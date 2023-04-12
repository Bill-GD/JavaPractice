package practice_w3;

import java.io.*;
import java.net.*;

public class PortScanner {
    public static void main(String[] args) {
        for (int i = 1; i <= 65535; i++)
            try {
                new ServerSocket(i).close();
            } catch (IOException ex) {
                System.out.println(i + ": error");
                continue;
            }
    }
}
