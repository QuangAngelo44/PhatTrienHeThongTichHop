package bai9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AuctionService service = (AuctionService) registry.lookup("AuctionService");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập tên của bạn: ");
            String userName = scanner.nextLine();

            while (true) {
                System.out.println("\n===== HỆ THỐNG ĐẤU GIÁ (User: " + userName + ") =====");
                System.out.println("1. Thêm sản phẩm đấu giá (Người bán)");
                System.out.println("2. Xem danh sách sản phẩm");
                System.out.println("3. Đặt giá thầu (Người đấu giá)");
                System.out.println("4. Thoát");
                System.out.print("Chọn menu: ");
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 1) {
                    System.out.print("Nhập tên sản phẩm: ");
                    String name = scanner.nextLine();
                    System.out.print("Nhập giá khởi điểm: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    service.addProduct(name, price);
                    System.out.println("Đã niêm yết sản phẩm thành công!");
                } else if (choice == 2) {
                    List<Product> list = service.getProducts();
                    System.out.println("\n--- DANH SÁCH SẢN PHẨM HIỆN CÓ ---");
                    if (list.isEmpty()) {
                        System.out.println("Chưa có sản phẩm nào đang đấu giá.");
                    } else {
                        for (Product p : list) {
                            System.out.println(p);
                        }
                    }
                } else if (choice == 3) {
                    System.out.print("Nhập mã sản phẩm muốn bid: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nhập giá bạn muốn đặt: ");
                    double bidPrice = Double.parseDouble(scanner.nextLine());
                    boolean success = service.placeBid(id, bidPrice, userName);
                    if (success) {
                        System.out.println("Đặt giá thầu thành công!");
                    } else {
                        System.out.println("Đặt giá thất bại! Giá của bạn phải cao hơn giá hiện tại.");
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