package practice_w1;

public class ThreadMethodDemo extends Thread {
    String name;

    ThreadMethodDemo(String name) {
        this.name = name;
    }

    public void run() {
        for (int i = 0; i < 50; i++)
            System.out.println(name + "(Alive?: " + this.isAlive() + "): " + i);
        System.out.println(name + " finished.");
    }

    public static void main(String[] args) {
        ThreadMethodDemo childThread = new ThreadMethodDemo("Child");
        childThread.start();

        for (int i = 0; i < 35; i++) {
            System.out.println("Main(Alive?: " + Thread.currentThread().isAlive() + "): " + i);
        }
        System.out.println("Main finished.");
        try {
            System.out.println("Waiting for child...");
            childThread.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("End of program");
    }
}
