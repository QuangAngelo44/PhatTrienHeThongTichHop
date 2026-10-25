package bai9;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private double highestPrice;
    private String highestBidder;

    public Product(int id, String name, double initialPrice) {
        this.id = id;
        this.name = name;
        this.highestPrice = initialPrice;
        this.highestBidder = "Chưa có";
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getHighestPrice() { return highestPrice; }
    public String getHighestBidder() { return highestBidder; }

    public void updateBid(double price, String bidder) {
        this.highestPrice = price;
        this.highestBidder = bidder;
    }

    @Override
    public String toString() {
        return "Mã SP: " + id + " | Tên: " + name + " | Giá hiện tại: " + highestPrice + "$ | Người giữ giá: " + highestBidder;
    }
}