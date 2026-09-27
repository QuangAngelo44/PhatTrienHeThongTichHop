package bai123;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HostAndUriInspector {
    public static void main(String[] args) {
        // Kiểm tra thiếu tham số
        if (args.length < 2) {
            System.err.println("Lỗi: Thiếu tham số.");
            System.err.println("Cú pháp: java HostAndUriInspector <hostname> <uri>");
            return;
        }

        String hostInput = args[0];
        String uriInput = args[1];

        System.out.println("=== 1. KIỂM TRA HOSTNAME: " + hostInput + " ===");
        try {
            InetAddress[] addresses = InetAddress.getAllByName(hostInput);
            for (InetAddress addr : addresses) {
                String ipType = (addr instanceof Inet4Address) ? "IPv4" 
                              : (addr instanceof Inet6Address) ? "IPv6" : "Unknown";
                System.out.println("- IP: " + addr.getHostAddress());
                System.out.println("  + Loại: " + ipType);
                System.out.println("  + Loopback: " + addr.isLoopbackAddress());
                System.out.println("  + Site Local (Private): " + addr.isSiteLocalAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("Lỗi phân giải Hostname: Không tìm thấy máy chủ hoặc địa chỉ IP (" + e.getMessage() + ")");
        }

        System.out.println("\n=== 2. PHÂN TÍCH URI: " + uriInput + " ===");
        try {
            URI uri = new URI(uriInput);
            System.out.println("- Scheme:   " + uri.getScheme());
            System.out.println("- Host:     " + uri.getHost());
            System.out.println("- Port:     " + (uri.getPort() == -1 ? "Mặc định (không chỉ định)" : uri.getPort()));
            System.out.println("- Path:     " + uri.getPath());
            System.out.println("- Query:    " + uri.getQuery());
            System.out.println("- Fragment: " + uri.getFragment());
        } catch (URISyntaxException e) {
            System.err.println("Lỗi cú pháp URI: " + e.getReason() + " tại vị trí " + e.getIndex());
        }
    }
}