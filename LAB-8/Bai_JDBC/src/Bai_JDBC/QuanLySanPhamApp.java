package Bai_JDBC;

import java.sql.*;
import java.util.Scanner;

public class QuanLySanPhamApp {
    private static final String URL = "jdbc:sqlite:sanpham.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Khởi tạo bảng SanPham (maSP, tenSP, gia, soluong)
    public static void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS SanPham ("
                   + "maSP TEXT PRIMARY KEY, "
                   + "tenSP TEXT NOT NULL, "
                   + "gia REAL, "
                   + "soluong INTEGER);";
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Lỗi khởi tạo bảng: " + e.getMessage());
        } finally {
            // Đóng tài nguyên nghiêm ngặt trong finally theo Bài tập 3
            closeResources(null, stmt, conn);
        }
    }

    // 1. CREATE: Thêm sản phẩm (Sử dụng PreparedStatement - Bài tập 4)
    public static void themSanPham(String maSP, String tenSP, double gia, int soLuong) {
        String sql = "INSERT INTO SanPham (maSP, tenSP, gia, soluong) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maSP);
            pstmt.setString(2, tenSP);
            pstmt.setDouble(3, gia);
            pstmt.setInt(4, soLuong);
            pstmt.executeUpdate();
            System.out.println("-> [CREATE] Đã thêm thành công sản phẩm: " + tenSP);
        } catch (SQLException e) {
            System.err.println("-> [Lỗi Thêm]: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // 2. READ: Liệt kê tất cả sản phẩm
    public static void hienThiTatCa() {
        String sql = "SELECT maSP, tenSP, gia, soluong FROM SanPham";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            System.out.println("\n----------------- DANH MỤC SẢN PHẨM -----------------");
            System.out.printf("%-8s | %-22s | %-15s | %-8s%n", "Mã SP", "Tên Sản Phẩm", "Giá (VNĐ)", "Số Lượng");
            System.out.println("-----------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-8s | %-22s | %,15.0f | %-8d%n",
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getDouble("gia"),
                        rs.getInt("soluong"));
            }
        } catch (SQLException e) {
            System.err.println("-> [Lỗi Xem]: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    // 2b. READ: Tìm kiếm theo tên sản phẩm
    public static void timKiemTheoTen(String tuKhoa) {
        String sql = "SELECT maSP, tenSP, gia, soluong FROM SanPham WHERE tenSP LIKE ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + tuKhoa + "%");
            rs = pstmt.executeQuery();

            System.out.println("\n-> KẾT QUẢ TÌM KIẾM THEO TỪ KHÓA: '" + tuKhoa + "'");
            while (rs.next()) {
                System.out.printf("   + [%s] %s - Giá: %,.0f VNĐ - Tồn kho: %d%n",
                        rs.getString("maSP"), rs.getString("tenSP"), rs.getDouble("gia"), rs.getInt("soluong"));
            }
        } catch (SQLException e) {
            System.err.println("-> [Lỗi Tìm kiếm]: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, conn);
        }
    }

    // 3. UPDATE: Cập nhật giá và số lượng theo mã sản phẩm
    public static void capNhatSanPham(String maSP, double giaMoi, int soLuongMoi) {
        String sql = "UPDATE SanPham SET gia = ?, soluong = ? WHERE maSP = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, giaMoi);
            pstmt.setInt(2, soLuongMoi);
            pstmt.setString(3, maSP);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> [UPDATE] Cập nhật thành công cho mã SP: " + maSP);
            } else {
                System.out.println("-> [UPDATE] Không tìm thấy mã SP: " + maSP);
            }
        } catch (SQLException e) {
            System.err.println("-> [Lỗi Cập nhật]: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // 4. DELETE: Xóa sản phẩm theo mã
    public static void xoaSanPham(String maSP) {
        String sql = "DELETE FROM SanPham WHERE maSP = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maSP);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("-> [DELETE] Đã xóa sản phẩm: " + maSP);
            } else {
                System.out.println("-> [DELETE] Không tìm thấy sản phẩm cần xóa: " + maSP);
            }
        } catch (SQLException e) {
            System.err.println("-> [Lỗi Xóa]: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, conn);
        }
    }

    // Hàm tiện ích giải phóng tài nguyên triệt để (Bài tập 3)
    private static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException ignored) {}
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException ignored) {}
        try {
            if (conn != null) conn.close();
        } catch (SQLException ignored) {}
    }

    public static void main(String[] args) {
        initDatabase();

        System.out.println("=== THỰC THI BÀI TẬP 1, 3, 4: CRUD SẢN PHẨM ===");
        // Thêm mới ít nhất 3 sản phẩm
        themSanPham("SP01", "Bàn phím cơ DareU", 650000, 20);
        themSanPham("SP02", "Chuột Logitech G102", 399000, 45);
        themSanPham("SP03", "Tai nghe HyperX Cloud", 1250000, 10);

        // Hiển thị danh mục
        hienThiTatCa();

        // Tìm kiếm
        timKiemTheoTen("Logitech");

        // Cập nhật giá và số lượng SP01
        capNhatSanPham("SP01", 590000, 30);

        // Xóa SP03
        xoaSanPham("SP03");

        // Hiển thị lại sau khi cập nhật và xóa
        hienThiTatCa();
    }
}