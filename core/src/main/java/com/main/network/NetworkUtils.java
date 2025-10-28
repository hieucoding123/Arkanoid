package com.main.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {
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

                        if (isLANAdress(ip))
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
    private static boolean isLANAdress(String ip) {
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

    // Get all ip address
    public static String getAllIPAddress() {
        StringBuilder result = new StringBuilder();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                result.append(networkInterface.getName()).append(": ");

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().length() == 4) {
                        result.append(address.getHostAddress());
                        if (addresses.hasMoreElements()) {
                            result.append(", ");
                        }
                    }
                }
                if (interfaces.hasMoreElements()) {
                    result.append("\n");
                }
            }
        } catch (SocketException e) {
            return "Error: " + e.getMessage();
        }

        return result.toString();
    }

    public static String formatIPForDisplay(String ip) {
        return "Server IP: " + ip + "\nPort: " + NetworkProtocol.TCP_PORT;
    }
}
