package TK1;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    public static final int PORT = 5000;
    
    // Danh sách các phòng chat mặc định của hệ thống
    public static final List<String> ROOM_LIST = Arrays.asList("Study", "Game", "TraiNghiem");
    
    // Quản lý client theo phòng (thread-safe): Tên phòng -> Danh sách ClientHandler
    public static final Map<String, List<ClientHandler>> rooms = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // Khởi tạo các phòng
        for (String room : ROOM_LIST) {
            rooms.put(room, new CopyOnWriteArrayList<>());
        }

        System.out.println("=== TCP CHAT SERVER DANG KHOI CHAY TAI PORT " + PORT + " ===");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client ket noi moi: " + socket.getRemoteSocketAddress());
                // Tạo luồng riêng phục vụ từng Client kết nối
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Loi khoi dong Server: " + e.getMessage());
        }
    }

    // Xử lý logic kết nối và giao tiếp cho từng Client
    static class ClientHandler implements Runnable {
        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String clientName;
        private String currentRoom = null;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

                // 1. Nhập tên Client
                out.println("Nhap ten cua ban:");
                String name = in.readLine();
                if (name == null || name.trim().isEmpty()) {
                    return;
                }
                this.clientName = name.trim();
                out.println("Xin chao " + clientName + "!");

                // Vòng lặp cho phép chọn/đổi phòng sau khi /quit
                while (true) {
                    selectRoom();
                    if (currentRoom == null) {
                        break; // Người dùng đã ngắt kết nối
                    }

                    // Nhận và phát tin nhắn trong phòng
                    String message;
                    while ((message = in.readLine()) != null) {
                        message = message.trim();
                        if (message.equalsIgnoreCase("/quit")) {
                            leaveCurrentRoom();
                            break; // Thoát ra ngoài vòng lặp để chọn lại phòng
                        } else if (message.equalsIgnoreCase("/exit")) {
                            leaveCurrentRoom();
                            out.println("Hen gap lai! Ngat ket noi...");
                            return; // Thoát khỏi run(), ngắt kết nối
                        } else if (!message.isEmpty()) {
                            // Phát tin nhắn tới các thành viên khác cùng phòng
                            broadcastToRoom("[" + clientName + "]: " + message);
                        }
                    }

                    if (message == null) {
                        leaveCurrentRoom();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Client " + clientName + " mat ket noi.");
            } finally {
                leaveCurrentRoom();
                try {
                    socket.close();
                } catch (IOException ignored) {}
                System.out.println("Dong ket noi voi: " + clientName);
            }
        }

        // Điều hướng chọn phòng chat
        private void selectRoom() throws IOException {
            while (true) {
                out.println("DANH_SACH_PHONG: " + String.join(", ", ROOM_LIST));
                out.println("Chon mot phong de tham gia (hoac go /exit de thoat):");
                String choice = in.readLine();
                if (choice == null || choice.equalsIgnoreCase("/exit")) {
                    currentRoom = null;
                    return;
                }
                choice = choice.trim();
                for (String r : ROOM_LIST) {
                    if (r.equalsIgnoreCase(choice)) {
                        this.currentRoom = r;
                        rooms.get(r).add(this);
                        out.println("Ban da vao phong [" + currentRoom + "]. Bat dau chat!");
                        out.println("(Go /quit de roi phong, go /exit de thoat chuong trinh)");
                        broadcastToRoom(">>> " + clientName + " da tham gia phong chat.");
                        return;
                    }
                }
                out.println("Phong khong hop le! Vui long chon lai dung ten phong.");
            }
        }

        // Rời phòng hiện tại
        private void leaveCurrentRoom() {
            if (currentRoom != null) {
                rooms.get(currentRoom).remove(this);
                broadcastToRoom("<<< " + clientName + " da roi khoi phong.");
                currentRoom = null;
            }
        }

        // Gửi tin nhắn đến các thành viên trong cùng phòng
        private void broadcastToRoom(String msg) {
            if (currentRoom != null) {
                List<ClientHandler> list = rooms.get(currentRoom);
                for (ClientHandler ch : list) {
                    if (ch != this) {
                        ch.out.println(msg);
                    }
                }
            }
        }
    }
}