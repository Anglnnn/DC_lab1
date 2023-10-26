package PartB;

import javax.swing.*;
import java.awt.*;

public class SecondWindow extends JDialog {
    private Thread t1, t2;
    CustomSemaphore semaphore = new CustomSemaphore();
    private JButton finishButtonOne;
    private JButton finishButtonTwo;
    private JButton beginButtonOne;
    private JButton beginButtonTwo;
    private JProgressBar statusSquare;
    private int counter;
    private JPanel contentPane;
    private JSlider slider;


    public SecondWindow() {

        JFrame win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(500,300);
        setModal(true);
        finishButtonOne.addActionListener(e -> {
            if(semaphore.isFree())
                return;

            statusSquare.setForeground(new Color(0,255,0));
            t1.interrupt();
            semaphore.free();

            beginButtonOne.setEnabled(true);
            finishButtonTwo.setEnabled(true);

        });
        finishButtonTwo.addActionListener(e -> {
            if(semaphore.isFree())
                return;


            statusSquare.setForeground(new Color(0,255,0));
            t2.interrupt();
            semaphore.free();

            beginButtonTwo.setEnabled(true);
            finishButtonOne.setEnabled(true);
        });
        beginButtonOne.addActionListener(e -> {
            if(semaphore.isLocked())
                return;
            semaphore.lock();

            t1 = new Thread(
                    () -> {
                        while (true) {
                            if(counter>10)
                                counter--;
                            slider.setValue(counter);
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException ex) {
                                System.out.println("Thread1 is interrupted");
                                break;
                            }
                        }
                    }
            );
            t1.setPriority(Thread.MIN_PRIORITY);

            beginButtonOne.setEnabled(false);
            finishButtonTwo.setEnabled(false);
            statusSquare.setForeground(new Color(255,0,0));
            t1.start();
        });
        beginButtonTwo.addActionListener(e -> {
            if(semaphore.isLocked())
                return;
            semaphore.lock();

            t2 = new Thread(
                    () -> {
                        while (true) {
                            if(counter<90)
                                counter++;
                            slider.setValue(counter);
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException ex) {
                                System.out.println("Thread2 is interrupted");
                                break;
                            }
                        }
                    }
            );
            t2.setPriority(Thread.MAX_PRIORITY);

            beginButtonTwo.setEnabled(false);
            finishButtonOne.setEnabled(false);
            statusSquare.setForeground(new Color(255,0,0));
            t2.start();
        });
        counter = slider.getValue();
        pack();
        win.setContentPane(contentPane);
        win.setVisible(true);
    }

    public static void main(String[] args) {
        new SecondWindow();
    }
}
