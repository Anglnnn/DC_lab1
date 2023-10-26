package PartA;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

public class FirstWindow extends JFrame implements Runnable {

    private static final int RUNNER_POSITION_1 = 10;
    private static final int RUNNER_POSITION_2 = 90;

    private int runnerPosition;
    private Thread thread1;
    private Thread thread2;

    private JTextField thread1PriorityTextField;
    private JTextField thread2PriorityTextField;
    private JSlider slider;
    private JButton startButton;


    public FirstWindow() {
        super("Part A");

        addWindowListener(new FirstWindowWindowListener());

        super.setBackground(Color.lightGray);
        runnerPosition = 0;

        thread1 = new Thread(this);
        thread1.setPriority(Thread.MAX_PRIORITY);

        thread2 = new Thread(this);
        thread2.setPriority(Thread.MIN_PRIORITY);

        thread1PriorityTextField = new JTextField(5);
        thread1PriorityTextField.setBorder(new EmptyBorder(10,10,10,25));
        thread1PriorityTextField.setText(Integer.toString(Thread.MAX_PRIORITY));

        thread2PriorityTextField = new JTextField(5);
        thread2PriorityTextField.setBorder(new EmptyBorder(10,25,10,10));
        thread2PriorityTextField.setText(Integer.toString(Thread.MIN_PRIORITY));

        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setValue(50);
        slider.createStandardLabels(10,1);
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBorder(new EmptyBorder(0,0,50,0));

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thread1.setPriority(Integer.parseInt(thread1PriorityTextField.getText()));
                thread2.setPriority(Integer.parseInt(thread2PriorityTextField.getText()));

                thread1.start();
                thread2.start();
            }
        });

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(slider, c);

        JLabel spaceLabel = new JLabel();
        spaceLabel.setSize(200,200);

        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(spaceLabel, c);

        thread1PriorityTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        thread2PriorityTextField.setFont(new Font("Arial", Font.PLAIN, 16));

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(thread1PriorityTextField, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 5, 0, 0);
        add(thread2PriorityTextField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weighty = 1.0;
        add(startButton, c);

        setSize(500, 300);

        setIconImage(new ImageIcon("icon.png").getImage());

        setFont(new Font("Arial", Font.PLAIN, 12));
        setBackground(Color.WHITE);
        slider.setForeground(Color.BLACK);
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(Color.CYAN);


        setVisible(true);
    }


    public class FirstWindowWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            thread1.interrupt();
            thread2.interrupt();

            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e1) {
            }


            e.getWindow().dispose();
        }
    }


    @Override
    public void run() {
        Random random = new Random();

        while (true) {
            synchronized (this) {
                runnerPosition = Thread.currentThread() == thread1 ? RUNNER_POSITION_1 : RUNNER_POSITION_2;

                if (random.nextBoolean()) {
                    int priority = Thread.currentThread().getPriority();
                    int sleepTime = 1000 / priority;
                    if (priority > 0) {
                        slider.setValue(slider.getValue() + priority);
                    } else if (priority < 0) {
                        slider.setValue(slider.getValue() - priority);
                    }

                    if (slider.getValue() == slider.getMinimum() || slider.getValue() == slider.getMaximum()) {
                        break;
                    }
                }

                System.out.println("Thread " + Thread.currentThread().getName() + " moved the slider to " + slider.getValue());
            }

            if (Thread.currentThread().isInterrupted()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        new FirstWindow();
    }
}