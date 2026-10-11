package bai123;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DateTimeTcpClient{
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 6000;

        try (
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Đã kết nối Server! Nhập DATE, TIME, DATETIME hoặc QUIT:");

            while (true) {
                System.out.print("> ");
                String cmd = scanner.nextLine();

                writer.write(cmd + "\n");
                writer.flush();

                String res = reader.readLine();
                if (res == null) {
                    System.out.println("Mất kết nối từ server.");
                    break;
                }
                System.out.println("Server trả về: " + res);

                if ("BYE".equals(res)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi kết nối TCP: " + e.getMessage());
        }
    }
}