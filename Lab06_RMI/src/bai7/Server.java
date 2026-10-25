package bai7;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            GeometryServiceimpl service = new GeometryServiceimpl();
            registry.rebind("GeometryService", service);
            System.out.println("Bài 7 - Geometry Server đang chạy...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}