package practice_w1;

import java.io.*;

public class ByteStreamInputDemo {
    public static void main(String[] args) {
        byte[] byteArray = new byte[100];
        for (byte i = 0; i < byteArray.length; i++)
            byteArray[i] = i;

        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteArray);

        try {
            for (int i = 0; i< byteArray.length; i++)
                System.out.print(byteInStream.read() + " ");
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
        System.out.println();
    }
}