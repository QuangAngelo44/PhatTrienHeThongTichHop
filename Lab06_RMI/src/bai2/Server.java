package bai2;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            CalculatorService service = new CalculatorServiceImpl();
            registry.rebind("CalculatorService", service);
            System.out.println("Bài 2 - Calculator Server đã sẵn sàng!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}