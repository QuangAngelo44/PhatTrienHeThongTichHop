package bai8;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class FlightServiceImpl extends UnicastRemoteObject implements FlightService {
    private Map<String, Integer> flights;

    public FlightServiceImpl() throws RemoteException {
        super();
        flights = new HashMap<>();
        flights.put("VN123", 50);
        flights.put("VJ456", 20);
        flights.put("QH789", 0);
    }

    @Override
    public synchronized boolean bookTicket(String flightNumber, int seats) throws RemoteException {
        if (!flights.containsKey(flightNumber)) return false;
        int currentSeats = flights.get(flightNumber);
        if (seats > 0 && currentSeats >= seats) {
            flights.put(flightNumber, currentSeats - seats);
            return true;
        }
        return false;
    }

    @Override
    public synchronized int availableSeats(String flightNumber) throws RemoteException {
        return flights.getOrDefault(flightNumber, -1);
    }

    @Override
    public synchronized boolean cancelBooking(String flightNumber, int seats) throws RemoteException {
        if (!flights.containsKey(flightNumber) || seats <= 0) return false;
        flights.put(flightNumber, flights.get(flightNumber) + seats);
        return true;
    }
}