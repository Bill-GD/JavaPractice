package practice_w3;

import java.io.*;
import java.net.*;

public class SocketServerDemo {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            while (true) {
                System.out.println("Waiting for the client request");
                Socket socket = serverSocket.accept();

                String message = "Hello to Client!";

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("From " + socket.getInetAddress().getHostAddress() + ": " + ois.readObject());

                ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
                socketOut.writeObject(message);
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.toString());
        }
    }
}
