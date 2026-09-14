package SocketTCP;

import java.io.*;
import java.net.*;

public class TCPEchoServer {
	public final static int serverPort = 1080;
	// Hoặc đổi sang cổng > 1024 như 1234 nếu port 7 yêu cầu quyền admin

	public static void main(String[] args) {
		try (ServerSocket ss = new ServerSocket(serverPort)) {
			System.out.println("Server da duoc tao...");
			while (true) {
				try {
					Socket s = ss.accept();
					OutputStream os = s.getOutputStream();
					InputStream is = s.getInputStream();
					int ch = 0;
					while (true) {
						ch = is.read();
						if (ch == -1)
							break;
						System.out.print((char) ch);
						os.write(ch);
					}
					s.close();
				} catch (IOException ie1) {
					System.out.println("Connection Error: " + ie1);
				}
			}
		} catch (IOException ie) {
			System.out.println("Server Creation Error: " + ie);
		}
	}
}
