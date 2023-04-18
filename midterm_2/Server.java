package midterm_2;

import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        int port = 1234;
        try {
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                Socket s = ss.accept();
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
                Cylinder c = new Cylinder(Double.parseDouble(br.readLine()), Double.parseDouble(br.readLine()));
                pw.println(c.getVolume());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
