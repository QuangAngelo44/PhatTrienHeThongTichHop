package Bai_JDBC;

import java.sql.*;

public class TransactionApp {
    private static final String URL = "jdbc:sqlite:sanpham.db";

    public static void chuyenHangHoaTransaction(String maSPXuat, String maSPNhap, int soLuongChuyen) {
        String sqlTru = "UPDATE SanPham SET soluong = soluong - ? WHERE maSP = ?";
        String sqlCong = "UPDATE SanPham SET soluong = soluong + ? WHERE maSP = ?";

        Connection conn = null;
        PreparedStatement pTru = null;
        PreparedStatement pCong = null;

        try {
            conn = DriverManager.getConnection(URL);

            // BƯỚC 1: TẮT AUTO-COMMIT ĐỂ BẮT ĐẦU TRANSACTION
            conn.setAutoCommit(false);
            System.out.println("-> Đã mở Transaction (AutoCommit = false)...");

            // BƯỚC 2: Trừ hàng kho nguồn
            pTru = conn.prepareStatement(sqlTru);
            pTru.setInt(1, soLuongChuyen);
            pTru.setString(2, maSPXuat);
            pTru.executeUpdate();

            // Kiểm tra nghiệp vụ giả định: Nếu số lượng chuyển <= 0 thì ném lỗi
            if (soLuongChuyen <= 0) {
                throw new SQLException("Số lượng chuyển không hợp lệ (phải > 0)!");
            }

            // BƯỚC 3: Cộng hàng kho đích
            pCong = conn.prepareStatement(sqlCong);
            pCong.setInt(1, soLuongChuyen);
            pCong.setString(2, maSPNhap);
            pCong.executeUpdate();

            // BƯỚC 4: TẤT CẢ THÀNH CÔNG THÌ GỌI COMMIT
            conn.commit();
            System.out.printf("==> GIAO DỊCH THÀNH CÔNG: Đã chuyển %d cái từ %s sang %s (COMMIT).%n", 
                    soLuongChuyen, maSPXuat, maSPNhap);

        } catch (SQLException e) {
            System.err.println("-> Gặp sự cố: " + e.getMessage());
            // BƯỚC 5: GẶP LỖI THÌ ROLLBACK TOÀN BỘ VỀ BAN ĐẦU
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("==> ĐÃ HỦY BỎ GIAO DỊCH (ROLLBACK) THÀNH CÔNG!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // BƯỚC 6: Trả lại trạng thái ban đầu và đóng tài nguyên
            try {
                if (pTru != null) pTru.close();
                if (pCong != null) pCong.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ignored) {}
        }
    }

    public static void main(String[] args) {
        System.out.println("=== THỬ NGHIỆM GIAO DỊCH THÀNH CÔNG ===");
        chuyenHangHoaTransaction("SP02", "SP01", 5);

        System.out.println("\n=== THỬ NGHIỆM GIAO DỊCH LỖI ĐỂ XEM ROLLBACK ===");
        // Thử chuyển số lượng -10 để kích hoạt rollback
        chuyenHangHoaTransaction("SP02", "SP01", -10);
    }
}