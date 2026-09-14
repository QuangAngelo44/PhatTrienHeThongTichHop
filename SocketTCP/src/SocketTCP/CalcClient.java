package SocketTCP;

import java.io.*;
import java.net.*;

public class CalcClient {
	public static void main(String[] args) {
		String hostname = "127.0.0.1";
		int port = 4444;
		try (Socket socket = new Socket(hostname, port)) {
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			writer.println("4"); // Gửi số 4 lên server để tính bình phương
			String response = reader.readLine();
			System.out.println("Ket qua nhan duoc tu Server: " + response + " là kết quả của phép tính bình phương");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}