package bai9;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            AuctionService service = new AuctionServiceImpl();
            registry.rebind("AuctionService", service);
            System.out.println("Bài 9 - Auction Server đang chạy...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}