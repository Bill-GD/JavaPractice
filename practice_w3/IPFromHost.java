package practice_w3;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPFromHost {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Enter a hostname");
            return;
        }

        try {
            InetAddress addr = InetAddress.getByName(args[0]);
            System.out.println("IP Address of '" + args[0] + "' is: " + addr.getHostAddress());
        } catch (UnknownHostException ex) {
            System.out.println("Host '" + args[0] + "' is unknown");
        }
    }
}
