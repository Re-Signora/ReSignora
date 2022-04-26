package work.chiro.game.application;

import work.chiro.game.scene.SceneClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Chiro
 */
public class MainWindow implements SceneClient {
    private static MainWindow mainWindow = null;
    private final JCheckBox musicOnCheckBox = new JCheckBox();
    private final JPanel mainPanel = new JPanel();
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
        JButton easyModeButton = new JButton();
        easyModeButton.addActionListener(e -> {
            Game.difficulty = Difficulty.Easy;
            nextScene();
        });
        easyModeButton.setText("简单模式");

        JButton mediumModeButton = new JButton();
        mediumModeButton.addActionListener(e -> {
            Game.difficulty = Difficulty.Medium;
            nextScene();
        });
        mediumModeButton.setText("普通模式");

        JButton hardModeButton = new JButton();
        hardModeButton.addActionListener(e -> {
            Game.difficulty = Difficulty.Hard;
            nextScene();
        });
        hardModeButton.setText("困难模式");

        JButton historyButton = new JButton();
        historyButton.addActionListener(e -> {
            JFrame historyWindowFrame = new JFrame("排行榜");
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
        historyButton.setText("排行榜");

        musicOnCheckBox.addActionListener(e -> {
            Game.musicEnable = musicOnCheckBox.isSelected();
            System.out.println("music enable: " + musicOnCheckBox.isSelected());
        });
        musicOnCheckBox.setText("打开音效");
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(5, 1, 10, 80));
        innerPanel.add(easyModeButton);
        innerPanel.add(mediumModeButton);
        innerPanel.add(hardModeButton);
        innerPanel.add(musicOnCheckBox);
        innerPanel.add(historyButton);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(Box.createHorizontalStrut(100), BorderLayout.WEST);
        mainPanel.add(Box.createHorizontalStrut(100), BorderLayout.EAST);
        mainPanel.add(Box.createVerticalStrut(100), BorderLayout.NORTH);
        mainPanel.add(Box.createVerticalStrut(100), BorderLayout.SOUTH);
        mainPanel.add(innerPanel, BorderLayout.CENTER);
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
