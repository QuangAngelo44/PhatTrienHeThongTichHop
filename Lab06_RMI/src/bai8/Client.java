package bai8;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            FlightService service = (FlightService) registry.lookup("FlightService");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n--- HỆ THỐNG ĐẶT VÉ MÁY BAY ---");
                System.out.println("1. Kiểm tra số ghế trống");
                System.out.println("2. Đặt vé");
                System.out.println("3. Hủy vé");
                System.out.println("4. Thoát");
                System.out.print("Chọn thao tác: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) {
                    System.out.print("Nhập mã chuyến bay (VN123, VJ456, QH789): ");
                    String flight = scanner.nextLine();
                    int seats = service.availableSeats(flight);
                    if (seats >= 0) {
                        System.out.println("Số ghế còn trống trên chuyến " + flight + ": " + seats);
                    } else {
                        System.out.println("Chuyến bay không tồn tại!");
                    }
                } else if (choice == 2) {
                    System.out.print("Nhập mã chuyến bay: ");
                    String flight = scanner.nextLine();
                    System.out.print("Nhập số vé muốn đặt: ");
                    int seats = Integer.parseInt(scanner.nextLine());
                    if (service.bookTicket(flight, seats)) {
                        System.out.println("Đặt vé thành công!");
                    } else {
                        System.out.println("Đặt vé thất bại! Không đủ ghế hoặc sai chuyến.");
                    }
                } else if (choice == 3) {
                    System.out.print("Nhập mã chuyến bay: ");
                    String flight = scanner.nextLine();
                    System.out.print("Nhập số vé muốn hủy: ");
                    int seats = Integer.parseInt(scanner.nextLine());
                    if (service.cancelBooking(flight, seats)) {
                        System.out.println("Hủy vé thành công!");
                    } else {
                        System.out.println("Hủy vé thất bại!");
                    }
                } else if (choice == 4) {
                    break;
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}