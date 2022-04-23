package edu.hitsz.application;

import edu.hitsz.scene.SceneClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Chiro
 */
public class HistoryWindow implements SceneClient {
    private static HistoryWindow historyWindow = null;
    private JPanel mainPanel;
    private JLabel difficulty;
    private JTable historyTable;
    private JButton deleteButton;
    private JButton modifyButton;
    private JButton restartButton;
    private final Object waitObject = new Object();

    public HistoryWindow() {
        restartButton.addActionListener(e -> nextScene());
    }

    public static HistoryWindow getInstance() {
        if (historyWindow == null) {
            synchronized (HistoryWindow.class) {
                historyWindow = new HistoryWindow();
            }
        }
        return historyWindow;
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
