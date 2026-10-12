package TK1;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ChatClient {
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            System.out.println("Da ket noi den Chat Server tai " + SERVER_HOST + ":" + SERVER_PORT);

            // Luồng 1: Liên tục lắng nghe tin nhắn từ Server đổ về Console
            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        System.out.println(serverMsg);
                    }
                } catch (IOException e) {
                    System.out.println("Mat ket noi den Server.");
                }
            });
            receiveThread.start();

            // Luồng 2 (Main Thread): Đọc nội dung nhập từ bàn phím và gửi lên Server
            String input;
            while ((input = console.readLine()) != null) {
                out.println(input);
                if (input.trim().equalsIgnoreCase("/exit")) {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Khong tim thay host: " + SERVER_HOST);
        } catch (IOException e) {
            System.err.println("Loi ket noi Socket: " + e.getMessage());
        }
    }
}