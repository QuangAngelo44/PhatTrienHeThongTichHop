package Bai_JDBC;

import java.sql.*;

public class NhanVienThongKeApp {
    private static final String URL = "jdbc:sqlite:nhanvien.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // 0. Xóa bảng cũ (nếu có) để tạo lại cấu trúc mới có cột lương
            stmt.executeUpdate("DROP TABLE IF EXISTS NhanVien;");

            // 1. Tạo bảng NhanVien có cột luong
            String createTable = "CREATE TABLE NhanVien ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "ten TEXT NOT NULL, "
                    + "chuc_vu TEXT, "
                    + "luong REAL);";
            stmt.executeUpdate(createTable);

            // Nạp dữ liệu mẫu
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu, luong) VALUES ('Võ Nhựt Quang', 'IT Specialist', 22000000);");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu, luong) VALUES ('Nguyễn Hoàng Nam', 'IT Specialist', 18000000);");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu, luong) VALUES ('Trần Thị Lan', 'Kế toán', 14500000);");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu, luong) VALUES ('Lê Văn Tùng', 'Kế toán', 15500000);");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu, luong) VALUES ('Phạm Mai Phương', 'Nhân sự', 13000000);");
            stmt.executeUpdate("INSERT INTO NhanVien (ten, chuc_vu, luong) VALUES ('Đặng Quốc Bảo', 'Giám đốc', 50000000);");

            System.out.println("-> Đã nạp danh sách nhân viên và mức lương mẫu.\n");

            // 2. TRUY VẤN: SUM, AVG, MIN, MAX
            String sqlAgg = "SELECT SUM(luong) AS tong_luong, "
                          + "       AVG(luong) AS luong_tb, "
                          + "       MIN(luong) AS luong_min, "
                          + "       MAX(luong) AS luong_max "
                          + "FROM NhanVien;";

            try (ResultSet rs = stmt.executeQuery(sqlAgg)) {
                if (rs.next()) {
                    System.out.println("========== BÁO CÁO TỔNG QUAN LƯƠNG CÔNG TY ==========");
                    System.out.printf("- Tổng quỹ lương (SUM)      : %,15.0f VNĐ%n", rs.getDouble("tong_luong"));
                    System.out.printf("- Mức lương trung bình (AVG) : %,15.0f VNĐ%n", rs.getDouble("luong_tb"));
                    System.out.printf("- Mức lương thấp nhất (MIN)  : %,15.0f VNĐ%n", rs.getDouble("luong_min"));
                    System.out.printf("- Mức lương cao nhất (MAX)   : %,15.0f VNĐ%n", rs.getDouble("luong_max"));
                }
            }

            // 3. TRUY VẤN: COUNT & GROUP BY theo chức vụ
            String sqlGroupBy = "SELECT chuc_vu, COUNT(*) AS so_luong, AVG(luong) AS luong_tb "
                              + "FROM NhanVien "
                              + "GROUP BY chuc_vu "
                              + "ORDER BY so_luong DESC;";

            System.out.println("\n====== PHÂN BỔ NHÂN LỰC THEO CHỨC VỤ (COUNT & GROUP BY) ======");
            System.out.printf("%-18s | %-10s | %-18s%n", "Chức Vụ", "Số Lượng", "Lương TB (VNĐ)");
            System.out.println("-------------------------------------------------------------");
            try (ResultSet rs = stmt.executeQuery(sqlGroupBy)) {
                while (rs.next()) {
                    System.out.printf("%-18s | %-7d người | %,15.0f VNĐ%n",
                            rs.getString("chuc_vu"),
                            rs.getInt("so_luong"),
                            rs.getDouble("luong_tb"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
        }
    }
}