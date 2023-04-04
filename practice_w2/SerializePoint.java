package practice_w2;

import java.io.*;

class Point implements Serializable {
    double x, y;

    Point() {
        x = y = 0;
    }

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    void showCoords() {
        System.out.println("(" + x + ", " + y + ")");
    }
}

public class SerializePoint {
    public static void main(String[] args) {
        try {
            FileOutputStream fos = new FileOutputStream("./points.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            Point point = new Point(4, 2);
            Point point2 = new Point(-5, 9);
            oos.writeObject(point);
            oos.writeObject(point2);
            oos.writeObject(null);

            FileInputStream fis = new FileInputStream("./points.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Point pointRead = null;
            while (true) {
                pointRead = (Point) ois.readObject();
                if (pointRead == null)
                    break;
                pointRead.showCoords();
            }

            oos.close();
            ois.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}