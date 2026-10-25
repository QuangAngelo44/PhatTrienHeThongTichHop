package bai4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            ContactService service = new ContactServiceImpl();
            registry.rebind("ContactService", service);
            System.out.println("Bài 4 - Contact Server đã sẵn sàng!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}