package practice_w3;

import java.net.*;
import java.io.*;

public class TestSocketClient {
    public static void main(String[] args) {
        int port = 1234;
        try {
            Socket socket = new Socket(InetAddress.getByName("localhost"), port);

            if (socket.isConnected())
                System.out.println("Connected using port " + port);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter first number: ");
            double firstNum = Double.parseDouble(br.readLine());
            System.out.print("Enter Second number: ");
            double secondNum = Double.parseDouble(br.readLine());

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            pw.println(firstNum);
            pw.println(secondNum);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (int i = 0; i < 4; i++)
                System.out.println(br.readLine());

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
