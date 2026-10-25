package bai6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            BankService service = new BankServiceImpl(1000000.0); // Khởi tạo số dư 1.000.000 VNĐ
            registry.rebind("BankService", service);
            System.out.println("Bài 6 - Bank Server đang chạy...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}