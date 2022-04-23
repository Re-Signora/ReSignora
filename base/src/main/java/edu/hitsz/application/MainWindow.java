package edu.hitsz.application;

import edu.hitsz.scene.SceneClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Chiro
 */
public class MainWindow implements SceneClient {
    private static MainWindow mainWindow = null;
    private JButton easyModeButton;
    private JButton mediumModeButton;
    private JButton hardModeButton;
    private JCheckBox musicOnCheckBox;
    private JLabel musicOnLabel;
    private JPanel mainPanel;
    private JButton HistoryButton;
    private final Object waitObject = new Object();

    public static MainWindow getInstance() {
        if (mainWindow == null) {
            synchronized (MainWindow.class) {
                mainWindow = new MainWindow();
            }
        }
        return mainWindow;
    }

    public MainWindow() {
        System.out.println("waitObject created at " + Thread.currentThread());
        easyModeButton.addActionListener(e -> {
            System.out.println(Thread.currentThread() + " clicked");
            nextScene();
        });

        HistoryButton.addActionListener(e -> {
            JFrame historyWindowFrame = new JFrame("HistoryWindow");
            historyWindowFrame.setContentPane(new HistoryWindow(false).getPanel());
            historyWindowFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    historyWindowFrame.dispose();
                }
            });
            historyWindowFrame.pack();
            historyWindowFrame.setVisible(true);
        });
    }

    @Override
    public JPanel getPanel() {
        return mainPanel;
    }

    @Override
    public Object getWaitObject() {
        return waitObject;
    }

    @Override
    public void nextScene() {
        synchronized (waitObject) {
            waitObject.notify();
        }
    }
}
