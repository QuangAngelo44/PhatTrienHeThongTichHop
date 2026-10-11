package bai4;
import java.io.*;
import java.net.*;

public class CalcServer {
    private static final int PORT = 25931;

    public static void main(String[] args) {
        System.out.println("[CALC SERVER] Đang lắng nghe tại cổng " + PORT + "...");
        //1. Khởi tạo kết nối mạng Ý nghĩa:ServerSocket(PORT): Mở cổng 65432 trên máy để chờ các client tìm đến kết nối.   
        //while (true): Vòng lặp vô tận giúp Server luôn sống và sẵn sàng phục vụ.      
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                //serverSocket.accept(): Hàm này sẽ dừng chờ (block) cho đến khi có một client kết nối tới thì mới nhả ra một đối tượng Socket clientSocket đại diện cho kết nối đó.
                // Xử lý từng client tuần tự hoặc đẩy vào thread riêng
                handleClient(clientSocket);
              //handleClient(clientSocket): Chuyển socket của client vừa vào cho hàm xử lý đọc/ghi dữ liệu.
            }
        } catch (IOException e) {
            System.err.println("Lỗi Server: " + e.getMessage());
        }
    }
    //Thiết lập luồng đọc/ghi dữ liệu
    //PrintWriter out (..., true):
    //while ((line = in.readLine()) != null): Đọc liên tục các lệnh từ client. 
    //Khi nào client đóng kết nối thì readLine() trả về null và vòng lặp tự thoát.   
    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Ý nghĩa:BufferedReader in: Đọc dữ liệu dạng văn bản theo từng dòng (readLine()) từ client gửi lên.  
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)//Gửi dữ liệu từ server về client. Cờ true bật chế độ tự động đẩy dữ liệu đi ngay lập tức (auto-flush) mỗi khi gọi println().  
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                String response = processCommand(line);
                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Client ngắt kết nối.");
        }
    }
    //Tách chuỗi và bắt lỗi định dạng (Dòng 37 – 42)
    private static String processCommand(String line) {
        String[] tokens = line.trim().split("\\s+");//dùng line.trim().split("\\s+") 
        //để cắt chuỗi theo khoảng trắng. Nếu mảng không đủ 4 phần tử hoặc từ đầu tiên không phải là CALC (dùng equalsIgnoreCase), 
        //Server lập tức trả về ERR INVALID_FORMAT. 
        //Ý nghĩa: Kiểm tra biến op (lấy từ tokens[1]) 
        // 1. Kiểm tra thiếu hoặc thừa thành phần
        if (tokens.length != 4 || !tokens[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }
        String op = tokens[1];
        String sNum1 = tokens[2];
        String sNum2 = tokens[3];
        //Kiểm tra toán tử hợp lệ (Dòng 49 – 51)
        // 2. Kiểm tra toán tử hợp lệ
        if (!op.equals("+") && !op.equals("-") && !op.equals("*") && !op.equals("/")) {
            return "ERR UNSUPPORTED_OPERATOR";
        }
        //Ép kiểu và bắt lỗi nhập chữ (Dòng 54 – 59)
      //Nếu toán tử không nằm trong danh sách 4 phép tính được hỗ trợ (+, -, *, /) (ví dụ nhập dấu %, ^) $\rightarrow$ Báo lỗi toán tử không hỗ trợ. 
        // 3. Kiểm tra định dạng số
        double n1, n2;
        try {
            n1 = Double.parseDouble(sNum1);
            n2 = Double.parseDouble(sNum2);
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }
        //Kiểm tra chia cho 0 và tính toán (Dòng 62 – 74)
        // 4. Kiểm tra chia cho 0
        //Ý nghĩa: Kiểm tra biến op (lấy từ tokens[1]). 
        //Nếu toán tử không nằm trong danh sách 4 phép tính được hỗ trợ (+, -, *, /) (ví dụ nhập dấu %, ^) $\rightarrow$ Báo lỗi toán tử không hỗ trợ.   
        if (op.equals("/") && n2 == 0) {
            return "ERR DIVIDE_BY_ZERO";
        }
        // 5. Tính toán
        //Ý nghĩa: Dùng Double.parseDouble() để chuyển đổi chuỗi sang số thực. 
        //Nếu người dùng gõ chữ cái (ví dụ CALC + a 2), hàm ép kiểu sẽ ném ra lỗi NumberFormatException. 
        //Khối catch bắt lại lỗi này và trả về ERR INVALID_NUMBER thay vì làm sập server.
        double res = 0;
        switch (op) {
            case "+": res = n1 + n2; break;
            case "-": res = n1 - n2; break;
            case "*": res = n1 * n2; break;
            case "/": res = n1 / n2; break;
        }
        //Rút gọn số nguyên khi trả về (Dòng 77 – 80)
        //nếu là số nguyên thì bỏ phần thập phân .0
        if (res == (long) res) {
            return "OK " + (long) res;
        }
        return "OK " + res;
    }
}
