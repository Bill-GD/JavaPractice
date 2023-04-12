package practice_w3;

import java.net.*;
import java.io.*;

public class TestSocketServer {
    public static void main(String[] args) {
        int port = 1234;
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                System.out.println("Listening at port " + port + "...");
                Socket socket = serverSocket.accept();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

                double firstNum = Double.parseDouble(br.readLine());
                double secondNum = Double.parseDouble(br.readLine());

                System.out.println("Received from " + socket.getInetAddress().getHostAddress());

                double sum = firstNum + secondNum;
                double sub = firstNum - secondNum;
                double mul = firstNum * secondNum;
                double div = firstNum / secondNum;

                pw.println("Sum: " + sum);
                pw.println("Sub: " + sub);
                pw.println("Mul: " + mul);
                pw.println("Div: " + div);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
