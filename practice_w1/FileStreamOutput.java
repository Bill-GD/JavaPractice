package practice_w1;

import java.io.*;
import java.util.Scanner;

public class FileStreamOutput {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String str = sc.nextLine(); sc.close();
        byte[] byteArray = str.getBytes();
        try {
            FileOutputStream fileOut = new FileOutputStream("./B3.txt");
            for (int i = 0; i < byteArray.length; i++)
                fileOut.write(byteArray[i]);
            FileInputStream fileIn = new FileInputStream("./B3.txt");
            while (fileIn.available() != 0)
                System.out.print((char) fileIn.read());

            fileIn.close();
            fileOut.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}