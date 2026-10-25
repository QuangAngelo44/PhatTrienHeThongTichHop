package bai1;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HelloService service = (HelloService) registry.lookup("HelloService");
            String response = service.sayHello();
            System.out.println("Kết quả từ Server: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}