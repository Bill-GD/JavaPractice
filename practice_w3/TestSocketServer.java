package practice_w3;

// imports to use ServerSocket, Reader, Writer
import java.net.*;
import java.io.*;

public class TestSocketServer {
    public static void main(String[] args) {
        // specifies port number
        int port = 1234;
        try {
            // create a server socket at the specified port
            ServerSocket serverSocket = new ServerSocket(port);

            // loop to continuously listen
            while (true) {
                System.out.println("Listening at port " + port + "...");
                // waits for a connection then accepts it
                Socket socket = serverSocket.accept();

                // creates reader to read from socket
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // creates writer to write to socket
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                // stores the inputs from socket
                double firstNum = Double.parseDouble(br.readLine());
                double secondNum = Double.parseDouble(br.readLine());

                // notifies (server-side) that the server has received inputs from port
                System.out.println("Received from " + socket.getInetAddress().getHostAddress() + '\n');

                // calculations
                double sum = firstNum + secondNum;
                double sub = firstNum - secondNum;
                double mul = firstNum * secondNum;
                double div = firstNum / secondNum;

                // sends the results back
                pw.println("Sum: " + sum);
                pw.println("Sub: " + sub);
                pw.println("Mul: " + mul);
                pw.println("Div: " + div);
            }
        } catch (Exception e) { // handles exception of ServerSocket()
            System.out.println(e.toString());
        }
    }
}
