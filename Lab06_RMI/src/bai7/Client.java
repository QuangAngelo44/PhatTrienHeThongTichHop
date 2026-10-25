package bai7;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            GeometryService service = (GeometryService) registry.lookup("GeometryService");

            Scanner scanner = new Scanner(System.in);
            System.out.println("--- TÍNH DIỆN TÍCH HÌNH HỌC ---");
            System.out.println("1. Diện tích Hình Chữ Nhật");
            System.out.println("2. Diện tích Hình Tròn");
            System.out.println("3. Diện tích Hình Tam Giác");
            System.out.print("Chọn hình cần tính: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Nhập chiều dài: ");
                    double w = scanner.nextDouble();
                    System.out.print("Nhập chiều rộng: ");
                    double h = scanner.nextDouble();
                    System.out.println("Diện tích HCN = " + service.rectangleArea(w, h));
                    break;
                case 2:
                    System.out.print("Nhập bán kính: ");
                    double r = scanner.nextDouble();
                    System.out.println("Diện tích Hình Tròn = " + service.circleArea(r));
                    break;
                case 3:
                    System.out.print("Nhập đáy: ");
                    double b = scanner.nextDouble();
                    System.out.print("Nhập chiều cao: ");
                    double height = scanner.nextDouble();
                    System.out.println("Diện tích Tam Giác = " + service.triangleArea(b, height));
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}