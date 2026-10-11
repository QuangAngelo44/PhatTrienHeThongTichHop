package bai123;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DigitClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 9998;

        try (
            Socket socket = new Socket(host, port);
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter serverWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Đã kết nối tới server. Nhập ký tự số (0-9) hoặc 'QUIT' để thoát:");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();

                serverWriter.write(input + "\n");
                serverWriter.flush();

                String response = serverReader.readLine();
                if (response == null) {
                    System.out.println("Mất kết nối từ server.");
                    break;
                }
                System.out.println("Server trả về: " + response);

                if ("BYE".equals(response)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi kết nối Socket: " + e.getMessage());
        }
    }
}