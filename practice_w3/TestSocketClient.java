package practice_w3;

// imports to use Socket, Reader, Writer
import java.net.*;
import java.io.*;

public class TestSocketClient {
    public static void main(String[] args) {
        // specifies port number
        int port = 1234;
        try {
            // creates a socket that connects to the specified port at localhost
            Socket socket = new Socket(InetAddress.getByName("localhost"), port);

            // notifies user if the socket is connected
            if (socket.isConnected())
                System.out.println("Connected using port " + port);

            // creates a reader to read input from console (user)
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // saves the inputs
            System.out.print("Enter first number: ");
            String firstNum = br.readLine();
            System.out.print("Enter Second number: ");
            String secondNum = br.readLine();

            // creates a writer to write to the socket
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // writes to socket
            pw.println(firstNum);
            pw.println(secondNum);

            // reader for reading from socket
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (int i = 0; i < 4; i++)
                System.out.println(br.readLine()); // output the received messages from socket

        } catch (Exception e) { // handles exceptions of Socket()
            System.out.println(e.toString());
        }
    }
}
