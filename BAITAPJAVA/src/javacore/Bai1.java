package javacore;

// 1. Khai báo thư viện để nhập dữ liệu
import java.util.Scanner;

public class Bai1 {
    public static void main(String[] args) {
        // 2. Tạo đối tượng Scanner
        Scanner sc = new Scanner(System.in);

        // 3. Nhập 3 số a, b, c từ bàn phím
        System.out.print("Nhập số a: ");
        int a = sc.nextInt();

        System.out.print("Nhập số b: ");
        int b = sc.nextInt();

        System.out.print("Nhập số c: ");
        int c = sc.nextInt();

        // 4. Tìm số lớn nhất (Max)
        int max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }

        // 5. Tìm số nhỏ nhất (Min)
        int min = a;
        if (b < min) {
            min = b;
        }
        if (c < min) {
            min = c;
        }

        // 6. Xuất kết quả
        System.out.println("Giá trị lớn nhất là: " + max);
        System.out.println("Giá trị nhỏ nhất là: " + min);

        // Đóng Scanner sau khi dùng xong
        sc.close();
    }
}