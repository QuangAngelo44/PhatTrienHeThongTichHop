package SocketTCP;

import java.io.*;
import java.net.*;

public class CalcServer {
	public static void main(String[] args) {
		int port = 4444;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("CalcServer đang chạy ở cổng " + port);
			while (true) {
				Socket socket = serverSocket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

				String line = reader.readLine();
				if (line != null) {
					int num = Integer.parseInt(line.trim());
					int result = num * num; // Ví dụ tính bình phương
					writer.println(result);
				}
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
