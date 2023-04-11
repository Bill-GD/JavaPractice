package practice_w3;

import java.net.*;

public class URLComponents {
    static void print(String s) {
        System.out.print(s);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Enter a URL");
            return;
        }
        try {
            URL url = new URL(args[0]);
            print(url.getProtocol() + '\n');
            print(url.getHost() + '\n');
            print(url.getPort() == -1 ? "" : url.getPort() + "\n");
            print(url.getFile() + '\n');
            print(url.getQuery() == null ? "" : url.getQuery() + '\n');
            print(url.getRef() == null ? "" : url.getRef() + '\n');
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
