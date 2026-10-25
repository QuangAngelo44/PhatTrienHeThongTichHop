package bai5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {
    public ChatServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized String sendMessage(String message) throws RemoteException {
        System.out.println("Client gửi: " + message);
        return "Server đã nhận được tin nhắn: \"" + message + "\"";
    }
}