package bai3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            PrimeService service = (PrimeService) registry.lookup("PrimeService");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập số cần kiểm tra: ");
            int n = scanner.nextInt();

            boolean result = service.isPrime(n);
            if (result) {
                System.out.println(n + " là số nguyên tố.");
            } else {
                System.out.println(n + " không phải là số nguyên tố.");
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}