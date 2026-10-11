package Bai_JDBC;

import java.sql.*;

public class TestNhanVien {
    private static final String URL = "jdbc:sqlite:nhanvien.db";

    public static void main(String[] args) {
        // Mở kết nối tới SQLite (tự động tạo file nhanvien.db nếu chưa có)
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            System.out.println("-> Kết nối SQLite thành công!");

            // 1. Tạo bảng NhanVien
            String sqlCreate = "CREATE TABLE IF NOT EXISTS NhanVien ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ten TEXT NOT NULL, "
                    + "chuc_vu TEXT);";
            stmt.executeUpdate(sqlCreate);
            System.out.println("-> Bảng NhanVien đã sẵn sàng.");

            // 2. Chèn dữ liệu mẫu
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu) VALUES ('Võ Nhựt Quang', 'IT Specialist');");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu) VALUES ('Nguyễn Văn A', 'Developer');");
            System.out.println("-> Đã thêm 2 nhân viên mẫu.");

            // 3. Truy vấn dữ liệu
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM NhanVien;")) {
                System.out.println("\n--- DANH SÁCH NHÂN VIÊN ---");
                while (rs.next()) {
                    System.out.printf("ID: %-3d | Tên: %-20s | Chức vụ: %s%n",
                            rs.getInt("id"), rs.getString("ten"), rs.getString("chuc_vu"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
    }
}