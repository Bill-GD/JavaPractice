package practice_w1;

public class PingPongRunnable implements Runnable {
    
    String word;
    int delayMillisecond;

    PingPongRunnable(String word, int delayMillisecond) {
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
        new Thread(new PingPongRunnable("ping", 10)).start();
        new Thread(new PingPongRunnable("PONG", 100)).start();
    }
}
