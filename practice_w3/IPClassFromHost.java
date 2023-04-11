package practice_w3;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPClassFromHost {
    static boolean isInRange(int num, int a, int b) {
        return a <= num && num <= b;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Enter a hostname");
            return;
        }

        try {
            String address = InetAddress.getByName(args[0]).getHostAddress();
            System.out.println("Hostname: " + args[0] + "\nAddress: " + address);
            // '.' in regex is special
            int addressOctet1 = Integer.parseInt(address.split("\\.")[0]);
            // int[] addressOctet = new int[4];
            // for (int i = 0; i < addressOctetString.length; i++)
            // addressOctet[i] = Integer.parseInt(addressOctetString[i]);

            String classIP = "Class: ";
            if (isInRange(addressOctet1, 1, 126))
                classIP += "A";
            else if (isInRange(addressOctet1, 128, 191))
                classIP += "B";
            else if (isInRange(addressOctet1, 192, 223))
                classIP += "C";
            else if (isInRange(addressOctet1, 224, 239))
                classIP += "D";
            else if (isInRange(addressOctet1, 240, 255))
                classIP += "E";
            else if (addressOctet1 == 127)
                classIP += "special";
            else
                classIP += "Invalid";

            System.out.println(classIP);
        } catch (UnknownHostException ex) {
            System.out.println("Host '" + args[0] + "' is unknown");
        }
    }
}
