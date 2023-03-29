package practice_w1;

import java.applet.*;
import java.awt.*;
import java.util.Date;

class ClockApplet extends Applet implements Runnable {
    Font f;
    Date d;
    Thread t;

    public ClockApplet() {
        f = new Font("Ariel", Font.BOLD, 30);
        d = new Date();
    }

    public void init() {
        resize(400, 400);
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

    public void stop() {
        if (t != null) {
            t.stop();
            t = null;
        }
    }

    public void run() {
        while (true) {
            d = new Date();
            repaint();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                return;
            }
        }
    }

    public void paint(Graphics g) {
        g.getFont();
        g.drawString(d.toString(), 10, 50);
    }
}
/*
<applet code="PracticeW1_3_1" width=400 height=400></applet>
 */