package bai123;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DateTimeUdpClient {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 7000; // Cổng khớp với DateTimeUdpServer

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {
            
            // Đặt thời gian chờ tối đa 3 giây để tránh bị treo vĩnh viễn nếu server chưa bật/tắt đột ngột
            socket.setSoTimeout(3000);
            InetAddress serverAddress = InetAddress.getByName(host);

            System.out.println("=== UDP TIME CLIENT SẴN SÀNG ===");
            System.out.println("Gõ các lệnh: DATE, TIME, DATETIME (hoặc gõ 'EXIT' để dừng client)");

            byte[] buffer = new byte[1024];

            while (true) {
                System.out.print("[UDP Client]> ");
                String cmd = scanner.nextLine();

                if ("EXIT".equalsIgnoreCase(cmd.trim())) {
                    System.out.println("Đã đóng client.");
                    break;
                }

                // 1. Gửi gói tin Datagram tới Server
                byte[] sendData = cmd.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, port);
                socket.send(sendPacket);

                // 2. Nhận kết quả phản hồi từ Server
                try {
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(receivePacket);

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
                    System.out.println("Server phản hồi: " + response);
                } catch (SocketTimeoutException e) {
                    System.err.println("Lỗi: Hết thời gian chờ (Timeout) - Không nhận được phản hồi từ Server!");
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi Socket UDP: " + e.getMessage());
        }
    }
}