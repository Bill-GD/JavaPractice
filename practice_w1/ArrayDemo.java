package practice_w1;
import java.util.ArrayList;

public class ArrayDemo {
    static void print(String s) {
        System.out.print(s);
    }

    static void sort(ArrayList<Double> array, int l, int r) {
        double p = array.get((l + r) / 2);
        int i = l, j = r;
        while (array.get(i) < p)
            i++;
        while (array.get(j) > p)
            j--;

        if (i <= j) {
            double temp = array.get(i);
            array.set(i, array.get(j));
            array.set(j, temp);
            i++;
            j--;
        }

        if (l < j) sort(array, l, j);
        if (i < r) sort(array, i, r);
    }

    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("No argument provided");
            return;
        }
        ArrayList<Double> array = new ArrayList<Double>();
        for (var arg : args) {
            double num = 0;
            try {
                num = Double.parseDouble(arg);
            } catch (Exception e) {
                continue;
            }
            array.add(num);
        }

        print("Current array:\n");
        for (var num : array)
            print(num + " ");
        print("\n");

        print("Sorted array:\n");
        sort(array, 0, array.size() - 1);
        for (var num : array)
            print(num + " ");
        print("\n");

        print("Average: ");
        double average = 0;
        for (var num : array)
            average += num;
        print((average / array.size()) + "");
    }
}