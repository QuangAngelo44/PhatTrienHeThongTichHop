package bai1;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostUriInspector {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java bai1.HostUriInspector <hostname> <URI>");
            return;
        }

        String hostInput = args[0];
        String uriInput = args[1];

        System.out.println("=== 1. PHÂN GIẢI HOSTNAME ===");
        try {
            InetAddress[] addresses = InetAddress.getAllByName(hostInput);
            System.out.println("Host: " + hostInput);
            for (InetAddress addr : addresses) {
                System.out.println("- IP: " + addr.getHostAddress());
                if (addr instanceof Inet4Address) {
                    System.out.println("  Version:    IPv4");
                } else if (addr instanceof Inet6Address) {
                    System.out.println("  Version:    IPv6");
                }
                System.out.println("  Loopback:   " + addr.isLoopbackAddress());
                System.out.println("  Site local: " + addr.isSiteLocalAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("Lỗi: Không thể phân giải host '" + hostInput + "'");
        }

        System.out.println("\n=== 2. PHÂN TÍCH URI ===");
        try {
            URI uri = new URI(uriInput);
            System.out.println("URI:      " + uriInput);
            System.out.println("Scheme:   " + uri.getScheme());
            System.out.println("Host:     " + uri.getHost());
            System.out.println("Port:     " + (uri.getPort() == -1 ? "Mặc định (không chỉ định)" : uri.getPort()));
            System.out.println("Path:     " + uri.getPath());
            System.out.println("Query:    " + uri.getQuery());
            System.out.println("Fragment: " + uri.getFragment());
        } catch (URISyntaxException e) {
            System.err.println("Lỗi cú pháp URI: " + e.getReason());
        }
    }
}