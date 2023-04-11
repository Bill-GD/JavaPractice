package practice_w3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class URLtoHTML {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Enter a URL");
            return;
        }
        try {
            URL url = new URL(args[0]);
            try (var br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line = br.readLine();

                while (line != null) {
                    System.out.println(line);
                    line = br.readLine();
                }
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
