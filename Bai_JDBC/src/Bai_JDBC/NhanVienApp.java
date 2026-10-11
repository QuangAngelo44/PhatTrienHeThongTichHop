package Bai_JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NhanVienApp {
    private static final String URL = "jdbc:sqlite:nhanvien.db";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // B1. Nạp driver và kết nối
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            System.out.println("B1: Kết nối SQLite thành công.");

            // B2. Tạo đối tượng Statement
            stmt = conn.createStatement();
            System.out.println("B2: Tạo Statement thành công.");

            // B3.1. Tạo bảng NhanVien
            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS NhanVien ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ten TEXT NOT NULL, "
                    + "chuc_vu TEXT);";
            stmt.executeUpdate(sqlCreateTable);
            System.out.println("B3.1: Đã tạo bảng NhanVien.");

            // B3.2. Thêm dữ liệu (ít nhất 3 nhân viên)
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu) VALUES ('Võ Nhựt Quang', 'IT Specialist');");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu) VALUES ('Trần Văn Bình', 'Developer');");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu) VALUES ('Lê Thị Hoa', 'Tester');");
            System.out.println("B3.2: Đã thêm 3 bản ghi nhân viên.");

            // B4. Truy vấn và hiển thị dữ liệu
            String sqlSelect = "SELECT id, ten, chuc_vu FROM NhanVien;";
            rs = stmt.executeQuery(sqlSelect);

            System.out.println("\nB4: --- DANH SÁCH NHÂN VIÊN ---");
            while (rs.next()) { // Không có dấu ; ở cuối
                int id = rs.getInt("id");
                String ten = rs.getString("ten");
                String chucVu = rs.getString("chuc_vu");
                System.out.printf("ID: %-3d | Tên: %-20s | Chức vụ: %s%n", id, ten, chucVu);
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy SQLite JDBC Driver: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
        } finally {
            // B5. Đóng tài nguyên theo thứ tự ngược lại trong finally
            try {
                if (rs != null) rs.close();
            } catch (SQLException ignored) {}

            try {
                if (stmt != null) stmt.close();
            } catch (SQLException ignored) {}

            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}

            System.out.println("\nB5: Đã đóng các kết nối và giải phóng tài nguyên an toàn.");
        }
    }
}