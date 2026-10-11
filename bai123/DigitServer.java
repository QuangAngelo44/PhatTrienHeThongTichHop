package bai123;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class DigitServer {
	private static final int PORT = 9998;
    private static final String[] DIGITS = {
        "Không", "Một", "Hai", "Ba", "Bốn", 
        "Năm", "Sáu", "Bảy", "Tám", "Chín"
    };

    public static void main(String[] args) {
        System.out.println("Digit Server đang chạy trên cổng " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client kết nối từ: " + clientSocket.getRemoteSocketAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ("QUIT".equals(line)) {
                    writer.write("BYE\n");
                    writer.flush();
                    break;
                }

                // Kiểm tra chính xác 1 ký tự và thuộc dải '0' -> '9'
                if (line.length() == 1 && Character.isDigit(line.charAt(0))) {
                    int digit = line.charAt(0) - '0';
                    writer.write(DIGITS[digit] + "\n");
                } else {
                    writer.write("ERR INVALID_DIGIT\n");
                }
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Client ngắt kết nối bất thường.");
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}