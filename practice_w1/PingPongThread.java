package practice_w1;

public class PingPongThread extends Thread {
    String word;
    int delayMillisecond;

    PingPongThread(String word, int delayMillisecond) {
        this.word = word;
        this.delayMillisecond = delayMillisecond;
    }

    public void run() {
        try {
            while (true) {
                System.out.print(word + " ");
                Thread.sleep(delayMillisecond);
            }
        } catch (InterruptedException e) {
            System.out.println(e.toString());
            return;
        }
    }

    public static void main(String[] args) {
        new PingPongThread("ping", 10).start();
        new PingPongThread("PONG", 100).start();
    }
}
