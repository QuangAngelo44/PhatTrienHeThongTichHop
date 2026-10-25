package bai9;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class AuctionServiceImpl extends UnicastRemoteObject implements AuctionService {
    private List<Product> productList;
    private int nextId = 1;

    public AuctionServiceImpl() throws RemoteException {
        super();
        productList = new ArrayList<>();
    }

    @Override
    public synchronized void addProduct(String name, double startingPrice) throws RemoteException {
        Product p = new Product(nextId++, name, startingPrice);
        productList.add(p);
        System.out.println("[Auction] Sản phẩm mới được thêm: " + name + " (Khởi điểm: " + startingPrice + "$)");
    }

    @Override
    public synchronized List<Product> getProducts() throws RemoteException {
        return new ArrayList<>(productList);
    }

    @Override
    public synchronized boolean placeBid(int productId, double bidPrice, String bidderName) throws RemoteException {
        for (Product p : productList) {
            if (p.getId() == productId) {
                if (bidPrice > p.getHighestPrice()) {
                    p.updateBid(bidPrice, bidderName);
                    System.out.println("[Auction] Có giá thầu mới cho SP [" + p.getName() + "]: " + bidPrice + "$ bởi " + bidderName);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}