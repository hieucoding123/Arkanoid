package com.main.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
/**
 * Utility class for network-related operations.
 * Provides methods to retrieve local and public IP addresses for multiplayer networking.
 *
 * <p>This class is designed to help with server discovery and connection setup
 * in local area networks (LAN) and over the internet.</p>
 */
public class NetworkUtils {
    /**
     * Retrieves the local IP address of the machine.
     * Prioritizes IPv4 addresses and filters out loopback addresses (127.0.0.1).
     *
     * <p>This method scans all active network interfaces and selects the most
     * appropriate IP address for LAN connections. It prioritizes addresses in
     * common private IP ranges (192.168.x.x, 10.x.x.x, 172.16-31.x.x).</p>
     *
     * <p><b>Use cases:</b></p>
     * <ul>
     *   <li>Displaying server IP to players in the same network</li>
     *   <li>Setting up local multiplayer sessions</li>
     *   <li>Testing network features on LAN</li>
     * </ul>
     *
     * @return the local IP address as a String (e.g., "192.168.1.100"),
     *         or "Unable to get IP" if retrieval fails
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().length() == 4) {
                        String ip = address.getHostAddress();

                        if (isLANAddress(ip))
                            return ip;
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.err.println("Error getting IP address");
            return "Unable to get IP";
        }
    }

    /**
     * Checks if an IP address belongs to a Local Area Network (LAN).
     *
     * <p>LAN addresses are private IP ranges defined by RFC 1918:</p>
     * <ul>
     *   <li>192.168.0.0 - 192.168.255.255 (192.168.0.0/16)</li>
     *   <li>10.0.0.0 - 10.255.255.255 (10.0.0.0/8)</li>
     *   <li>172.16.0.0 - 172.31.255.255 (172.16.0.0/12)</li>
     * </ul>
     *
     * @param ip the IP address to check (e.g., "192.168.1.100")
     * @return true if the IP is a LAN address, false otherwise
     */
    private static boolean isLANAddress(String ip) {
        if (ip == null)
            return false;

        String[] parts = ip.split("\\.");
        if (parts.length != 4)
            return false;

        try {
            int first  = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);

            // 192.168.x.x
            if (first == 192 && second == 168) return true;

            // 10.x.x.x
            if (first == 10) return true;

            // 172.16.x.x - 172.31.x.x
            if (first == 172 && second >= 16 && second <= 31) return true;
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }
}
