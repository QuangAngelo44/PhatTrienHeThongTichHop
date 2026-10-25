package bai3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            PrimeService service = new PrimeServiceImpl();
            registry.rebind("PrimeService", service);
            System.out.println("Bài 3 - Prime Server đã sẵn sàng!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}