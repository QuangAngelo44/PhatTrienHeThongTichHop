package bai9;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AuctionService extends Remote {
    void addProduct(String name, double startingPrice) throws RemoteException;
    List<Product> getProducts() throws RemoteException;
    boolean placeBid(int productId, double bidPrice, String bidderName) throws RemoteException;
}