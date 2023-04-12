package practice_w3;

import java.net.*;
import java.io.*;

public class ClientToUpper {
    public static void main(String[] args) {
        int port = 1000;
        try {
            Socket socket = new Socket(InetAddress.getByName("localhost"), port);

            if (socket.isConnected())
                System.out.println("Connected");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter a string: ");
            String stringToSend = br.readLine();

            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            pw.println(stringToSend);
            pw.flush();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(br.readLine());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
