package practice_w1;

import java.io.*;

public class ByteStreamOutputDemo {
    public static void main(String[] args) {
        byte[] byteArray1 = new byte[127];
        for (byte i = 0; i < byteArray1.length; i++)
            byteArray1[i] = i;
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteArray1);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        for (int i = 0; i < byteArray1.length; i++) {
            byteOut.write(byteIn.read());
            byteOut.write((byte)' ');
        }
        System.out.println(byteOut.toString());
    }
}