package practice_w4;

import java.util.ArrayList;
import java.util.Random;

class Student {
    private String id = "", name = "";
    private double score = 0.0;

    Student() {
    }

    Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    Student(String id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", score=" + score;
    }
}

public class SinhVien {
    static void sortStudentScore(ArrayList<Student> students) {
        for (int i = 0; i < students.size() - 1; i++)
            for (int j = i + 1; j < students.size(); j++)
                if (students.get(i).getScore() < students.get(j).getScore()) {
                    Student temp = students.get(i);
                    students.set(i, students.get(j));
                    students.set(j, temp);
                }
    }

    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<Student>();
        for (int i = 0; i < 10; i++) {
            Random r = new Random();
            students.add(new Student("id-" + i, "Sinh Vien " + i, r.nextInt(11)));
        }
        sortStudentScore(students);
        for (Student s : students) {
            System.out.println(s);
        }
    }
}
