package bai5;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            ChatService service = new ChatServiceImpl();
            registry.rebind("ChatService", service);
            System.out.println("Bài 5 - Chat Server đang chạy...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}