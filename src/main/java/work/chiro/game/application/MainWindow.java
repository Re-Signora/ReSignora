package work.chiro.game.application;

import work.chiro.game.scene.SceneClient;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Chiro
 */
public class MainWindow implements SceneClient {
    private static MainWindow mainWindow = null;
    private final JButton easyModeButton = new JButton();
    private final JButton mediumModeButton = new JButton();
    private final JButton hardModeButton = new JButton();
    private final JCheckBox musicOnCheckBox = new JCheckBox();
    private final JPanel mainPanel = new JPanel();
    private final JButton historyButton = new JButton();
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
            Game.difficulty = Difficulty.Easy;
            nextScene();
        });
        easyModeButton.setText("简单模式");

        mediumModeButton.addActionListener(e -> {
            Game.difficulty = Difficulty.Medium;
            nextScene();
        });
        mediumModeButton.setText("普通模式");

        hardModeButton.addActionListener(e -> {
            Game.difficulty = Difficulty.Hard;
            nextScene();
        });
        hardModeButton.setText("困难模式");
        Box startBox = Box.createVerticalBox();

        startBox.add(Box.createVerticalStrut(100));
        startBox.add(easyModeButton);
        startBox.add(Box.createVerticalStrut(100));
        startBox.add(mediumModeButton);
        startBox.add(Box.createVerticalStrut(100));
        startBox.add(hardModeButton);

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
        startBox.add(Box.createVerticalStrut(100));
        startBox.add(musicOnCheckBox);
        startBox.add(Box.createVerticalStrut(100));
        startBox.add(historyButton);

        mainPanel.add(startBox);
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
