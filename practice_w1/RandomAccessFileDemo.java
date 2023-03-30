package practice_w1;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class RandomAccessFileDemo {
    public static void main(String[] args) {
        try {
            // Random array
            int maxNum = (new Random().nextInt(50)) + 1;

            RandomAccessFile raf = new RandomAccessFile("./B4.dat", "rw");
            byte[] byteArrayIn = new byte[maxNum];
            for (int i = 0; i < byteArrayIn.length; i++) {
                int randomNum = new Random().nextInt(99) + 1;
                byteArrayIn[i] = (byte) (randomNum);
            }
            raf.write(byteArrayIn);
            raf.close();

            raf = new RandomAccessFile("./B4.dat", "r");
            byte[] byteArrayOut = new byte[maxNum];

            for (int i = 0; i < byteArrayOut.length; i++)
                byteArrayOut[i] = (byte) raf.read();
            Arrays.sort(byteArrayOut);
            for (int i = 0; i < byteArrayOut.length; i++)
                System.out.print(byteArrayOut[i] + " ");

            raf.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}