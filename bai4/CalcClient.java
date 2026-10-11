package bai4;
import java.io.*;
import java.net.*;

public class CalcClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 25931;

        String[] testCases = {
            "CALC - 10 5",      // Kì vọng: OK 
            "CALC / 10 0",        // Kì vọng: ERR DIVIDE_BY_ZERO
            "CALC % 10 3",        // Kì vọng: ERR UNSUPPORTED_OPERATOR
            "CALC + a 2",         // Kì vọng: ERR INVALID_NUMBER
            "CALC + 10"           // Kì vọng: ERR INVALID_FORMAT
        };

        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            for (String tc : testCases) {
                out.println(tc);
                String response = in.readLine();
                System.out.println("Gửi phép tính lên server    : " + tc);
                System.out.println("Server Phản hồi: " + response);
                System.out.println("------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Lỗi kết nối tới Server: " + e.getMessage());
        }
    }
}