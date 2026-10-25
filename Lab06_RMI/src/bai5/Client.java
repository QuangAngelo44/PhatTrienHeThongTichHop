package bai5;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ChatService service = (ChatService) registry.lookup("ChatService");

            Scanner scanner = new Scanner(System.in);
            System.out.println("--- ỨNG DỤNG CHAT (Gõ 'exit' để dừng) ---");
            while (true) {
                System.out.print("Bạn: ");
                String msg = scanner.nextLine();
                if ("exit".equalsIgnoreCase(msg)) break;

                String reply = service.sendMessage(msg);
                System.out.println("Phản hồi: " + reply);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}