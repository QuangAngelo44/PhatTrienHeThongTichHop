package bai8;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            FlightService service = new FlightServiceImpl();
            registry.rebind("FlightService", service);
            System.out.println("Bài 8 - Flight Server đang chạy...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}