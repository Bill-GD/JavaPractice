package practice_w3;

import java.io.*;
import java.net.*;

public class ServerToUpper {
    public static void main(String[] args) {
        int port = 1000;
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                System.out.println("Listening at port " + port + "...");
                Socket socket = serverSocket.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String fromClient = br.readLine().toUpperCase();

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                pw.println(fromClient);

                // System.out.println("From " + socket.getInetAddress().getHostAddress() + ": " + fromClient);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}
