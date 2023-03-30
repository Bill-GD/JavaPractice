package practice_w1;

import java.io.*;

public class FileReadWrite {
    public static void main(String[] args) throws IOException {
        // set up console reader and writer
        // read character bytes
        InputStreamReader isr = new InputStreamReader(System.in, System.console().charset());
        BufferedReader br = new BufferedReader(isr); // can read line -> string
        OutputStreamWriter osw = new OutputStreamWriter(System.out, System.console().charset());
        try {
            osw.write("Enter a text (.txt) file path: ");
            osw.flush();

            String path = br.readLine();
            if (!path.contains(".txt")) {
                osw.write("Not a .txt file.\n");
                osw.flush();
                return;
            }
            FileWriter fw = new FileWriter(path);
            while (true) {
                osw.write("(Type 'stop' to stop) Enter a string: ");
                osw.flush();
                String line = br.readLine().toUpperCase();
                if (line.equals("STOP"))
                    break;
                fw.write(line + '\n');
                fw.flush(); // needed to actually write (flush the buffer: stream -> file)
            }

            fw.close();

            FileReader fr = new FileReader(path);
            int c;
            while ((c = fr.read()) != -1) {
                osw.write((char) c);
                osw.flush();
            }
            fr.close();
        } catch (Exception e) {
            osw.write(e.toString());
            osw.flush();
        }
    }
}
