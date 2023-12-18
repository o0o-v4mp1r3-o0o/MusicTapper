import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ColorFadingDemo extends JPanel implements ActionListener {

        Timer timer = new Timer(0, this);
        float alpha = 0f;
        long startTime = -1;
        public static final long RUNNING_TIME = 500;
        public int x;
        public int y;

        public void start() {

            timer.setDelay(5);
            timer.start();
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (startTime < 0) {
                startTime = System.currentTimeMillis();
            } else {
                long time = System.currentTimeMillis();
                long duration = time - startTime;
                if (duration >= RUNNING_TIME) {
                    startTime = -1;
                    ((Timer) e.getSource()).stop();
                    alpha = 0f;
                } else {
                    alpha = 1f - ((float) duration / (float) RUNNING_TIME);
                }
                repaint();
            }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
            g2d.fillOval(x,y,50,50);
            g2d.dispose();

        }
    }
