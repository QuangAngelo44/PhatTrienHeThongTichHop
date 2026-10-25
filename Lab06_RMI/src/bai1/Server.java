package bai1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            HelloService service = new HelloServiceImpl();
            registry.rebind("HelloService", service);
            System.out.println("Bài 1 - Server đã sẵn sàng!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}