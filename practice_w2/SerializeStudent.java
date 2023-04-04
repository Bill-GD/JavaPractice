package practice_w2;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

class Student implements Serializable {
    double score1, score2, score3, average;

    Student(double score1, double score2, double score3) {
        this.score1 = score1;
        this.score2 = score2;
        this.score3 = score3;
        average = (score1 + score2 + score3) / 3;
    }
}

public class SerializeStudent {
    public static void main(String[] args) {
        try {
            // create streams for serialization
            FileOutputStream fos = new FileOutputStream("./students.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            ArrayList<Student> studentList = new ArrayList<Student>();
            int maxStudent = new Random().nextInt(20) + 1;
            for (int i = 0; i < maxStudent; i++) {
                studentList.add(new Student(
                        new Random().nextDouble(11),
                        new Random().nextDouble(11),
                        new Random().nextDouble(11)));
            }
            for (Student s : studentList)
                oos.writeObject(s);
            studentList = null;
            fos.close();

            // create streams for deserialization
            FileInputStream fis = new FileInputStream("./students.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Student[] studentArray = new Student[maxStudent];
            for (int i = 0; i < maxStudent; i++)
                studentArray[i] = (Student) ois.readObject();

            // sort
            for (int i = 0; i < maxStudent - 1; i++)
                for (int j = 0; j < maxStudent - i - 1; j++)
                    if (studentArray[j].average >= studentArray[j + 1].average) {
                        Student t = studentArray[j];
                        studentArray[j] = studentArray[j + 1];
                        studentArray[j + 1] = t;
                    }

            // new streams for serialization
            fos = new FileOutputStream("./sxsv.dat");
            oos = new ObjectOutputStream(fos);
            for (Student s : studentArray) {
                oos.writeObject(s);
                System.out.println(s.average);
            }

            fos.close();
            oos.close();
            ois.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
