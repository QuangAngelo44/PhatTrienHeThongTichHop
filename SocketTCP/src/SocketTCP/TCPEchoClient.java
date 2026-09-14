package SocketTCP;

import java.io.*;
import java.net.*;

public class TCPEchoClient {
	public static final String serverIP = "127.0.0.1"; // hoac localhost hoac 172.0.0.2 ... 3 cung dươc vì nó là địa chỉ
														// (loopback)
	public static final int serverPort = 1080; // Phải trùng với port của Server

	public static void main(String[] args) throws InterruptedException, IOException {
		Socket s = null;
		try {
			s = new Socket(serverIP, serverPort);
			System.out.println("Client da duoc tao.");
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			for (int i = '0'; i <= '9'; i++) { // Gửi '0'->'9' den server
				os.write(i);
				int ch = is.read();
				System.out.print((char) ch);
				Thread.sleep(2000);
			}
		} catch (IOException ie) {
			System.out.println("Error: Can NOT create socket");
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}
}
