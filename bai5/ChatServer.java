package bai5;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.*;

public class ChatServer {
    private static final int PORT = 65433;
    private static final int THREAD_POOL_SIZE = 50;

    // Lưu danh sách user thread-safe: nickname -> PrintWriter
    private static final Map<String, PrintWriter> activeClients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        System.out.println("[CHAT SERVER] Đang chạy với Thread Pool trên cổng " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Lỗi Server: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    // Broadcast tin nhắn cho tất cả các client khác
    public static void broadcast(String message, String senderNick) {
        for (Map.Entry<String, PrintWriter> entry : activeClients.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(senderNick)) {
                entry.getValue().println(message);
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private String nickname = null;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Giai đoạn 1: Đăng ký Nickname duy nhất
                while (true) {
                    out.println("Vui long dang ky: NICK <ten_cua_ban>");
                    String line = in.readLine();
                    if (line == null) return;

                    String[] tokens = line.trim().split("\\s+", 2);
                    if (tokens.length == 2 && tokens[0].equalsIgnoreCase("NICK")) {
                        String candidate = tokens[1].trim();
                        synchronized (activeClients) {
                            if (candidate.isEmpty() || activeClients.containsKey(candidate)) {
                                out.println("ERR NICK_TAKEN");
                            } else {
                                nickname = candidate;
                                activeClients.put(nickname, out);
                                out.println("OK_NICK");
                                break;
                            }
                        }
                    } else {
                        out.println("ERR REGISTER_FIRST");
                    }
                }

                System.out.println("[INFO] " + nickname + " da ket noi.");
                broadcast("[SYSTEM] " + nickname + " da tham gia phong chat.", nickname);

                // Giai đoạn 2: Xử lý các lệnh USERS, MSG, QUIT
                String line;
                while ((line = in.readLine()) != null) {
                    String[] tokens = line.trim().split("\\s+", 2);
                    if (tokens.length == 0 || tokens[0].isEmpty()) continue;

                    String cmd = tokens[0].toUpperCase();

                    if (cmd.equals("USERS")) {
                        String list = String.join(", ", activeClients.keySet());
                        out.println("ONLINE: " + list);
                    } else if (cmd.equals("MSG")) {
                        if (tokens.length > 1 && !tokens[1].trim().isEmpty()) {
                            broadcast("[" + nickname + "]: " + tokens[1].trim(), nickname);
                        } else {
                            out.println("ERR EMPTY_MSG");
                        }
                    } else if (cmd.equals("QUIT")) {
                        out.println("Tam biet!");
                        break;
                    } else {
                        out.println("ERR UNKNOWN_COMMAND (Dung: MSG <tin_nhan>, USERS, QUIT)");
                    }
                }

            } catch (IOException e) {
                // Xử lý khi client ngắt kết nối đột ngột
            } finally {
                if (nickname != null) {
                    activeClients.remove(nickname);
                    broadcast("[SYSTEM] " + nickname + " da roi phong chat.", nickname);
                    System.out.println("[INFO] " + nickname + " da ngat ket noi.");
                }
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}