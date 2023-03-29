package practice_w1;

// console clock

public class ClockConsole {
    public static void main(String[] args) {
        long second = 0;
        long hour = 0, minute = 0;
        while (true) {
            while (second >= 60) {
                minute++;
                second -= 60;
            }
            while (minute >= 60) {
                hour++;
                minute -= 60;
            }
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print("Time: " + (hour < 10 ? ("0" + hour) : hour) + ":"
                        + (minute < 10 ? ("0" + minute) : minute) + ":"
                        + (second < 10 ? ("0" + second) : second));
                Thread.currentThread().sleep(1000);
            } catch (Exception e) {
                System.out.println(e.toString());
                return;
            }
            second += 1;
        }
    }
}
