package edu.hitsz.application;

import edu.hitsz.scene.SceneClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Chiro
 */
public class MainWindow implements SceneClient {
    private JButton easyModeButton;
    private JButton mediumModeButton;
    private JButton hardModeButton;
    private JCheckBox musicOnCheckBox;
    private JLabel musicOnLabel;
    private JPanel mainPanel;
    private final Object waitObject;

    public MainWindow() {
        System.out.println("waitObject created at " + Thread.currentThread());
        waitObject = new Object();
        easyModeButton.addActionListener(e -> {
            System.out.println(Thread.currentThread() + "clicked");
            waitObject.notify();
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
}
