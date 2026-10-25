package bai6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            BankService service = (BankService) registry.lookup("BankService");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\n--- DỊCH VỤ NGÂN HÀNG ---");
                System.out.println("1. Xem số dư");
                System.out.println("2. Nạp tiền");
                System.out.println("3. Rút tiền");
                System.out.println("4. Thoát");
                System.out.print("Chọn chức năng: ");
                int choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.println("Số dư hiện tại: " + service.getBalance() + " VNĐ");
                } else if (choice == 2) {
                    System.out.print("Nhập số tiền cần gửi: ");
                    double amount = scanner.nextDouble();
                    service.deposit(amount);
                    System.out.println("Nạp tiền thành công! Số dư mới: " + service.getBalance());
                } else if (choice == 3) {
                    System.out.print("Nhập số tiền cần rút: ");
                    double amount = scanner.nextDouble();
                    if (service.withdraw(amount)) {
                        System.out.println("Rút tiền thành công! Số dư mới: " + service.getBalance());
                    } else {
                        System.out.println("Rút tiền thất bại! Số dư không đủ hoặc số tiền không hợp lệ.");
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