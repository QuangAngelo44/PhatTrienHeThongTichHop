package javacore;
import java.util.Scanner;
public class chanle {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Kiem tra so chan le");
		System.out.println("Vui long nhap so de kiem tra");
		int so = scanner.nextInt();
		System.out.println();
		if ( so % 2 == 0) {
			System.out.println("So "+ so +" ban nhap la so chan");
			System.out.println();
		}
		else {
			
			System.out.println("So " + so +" Ban nhap la so le");
		}
	}
}
