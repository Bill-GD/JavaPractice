package midterm_2;

import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        int port = 1234;
        try {
            Socket s = new Socket("localhost", port);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
            pw.println(br.readLine());
            pw.println(br.readLine());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println(br.readLine());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
