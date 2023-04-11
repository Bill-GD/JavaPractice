package practice_w3;

import java.io.*;
import java.net.*;

public class SocketClientDemo {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), 8000);

            if (socket.isConnected()) {
                System.out.println("Connected");
            }
            String message = "Hello to Server!";

            ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
            socketOut.writeObject(message);;

            // BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // System.out.println(br.readLine());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("From server: " + ois.readObject());

        } catch (Exception ex) {
            System.out.println("Error: " + ex.toString());
        }
    }
}
