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
    private JButton easyModeButton;
    private JButton mediumModeButton;
    private JButton hardModeButton;
    private JCheckBox musicOnCheckBox;
    private JPanel mainPanel;
    private JButton historyButton;
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

        mediumModeButton.addActionListener(e -> {
            Game.difficulty = Difficulty.Medium;
            nextScene();
        });

        hardModeButton.addActionListener(e -> {
            Game.difficulty = Difficulty.Hard;
            nextScene();
        });

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

        musicOnCheckBox.addActionListener(e -> {
            Game.musicEnable = musicOnCheckBox.isSelected();
            System.out.println("music enable: " + musicOnCheckBox.isSelected());
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
