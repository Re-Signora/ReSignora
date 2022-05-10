package work.chiro.game.windows;

import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.scene.AbstractSceneClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Chiro
 */
public class MainWindow extends AbstractSceneClient {
    private static MainWindow mainWindow = null;
    private final JCheckBox musicOnCheckBox = new JCheckBox();
    private final JCheckBox autoShootCheckBox = new JCheckBox();
    private final JPanel mainPanel = new JPanel();

    public static MainWindow getInstance() {
        if (mainWindow == null) {
            synchronized (MainWindow.class) {
                mainWindow = new MainWindow();
            }
        }
        return mainWindow;
    }

    public MainWindow() {
        JButton easyModeButton = new JButton();
        easyModeButton.addActionListener(e -> {
            RunningConfig.difficulty = Difficulty.Easy;
            nextScene(GameWindow.class);
        });
        easyModeButton.setText("简单模式");

        JButton mediumModeButton = new JButton();
        mediumModeButton.addActionListener(e -> {
            RunningConfig.difficulty = Difficulty.Medium;
            nextScene(GameWindow.class);
        });
        mediumModeButton.setText("普通模式");

        JButton hardModeButton = new JButton();
        hardModeButton.addActionListener(e -> {
            RunningConfig.difficulty = Difficulty.Hard;
            nextScene(GameWindow.class);
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
            RunningConfig.musicEnable = musicOnCheckBox.isSelected();
            System.out.println("music enable: " + musicOnCheckBox.isSelected());
        });
        musicOnCheckBox.setSelected(true);
        musicOnCheckBox.setText("打开音效");
        autoShootCheckBox.addActionListener(e -> {
            RunningConfig.autoShoot = autoShootCheckBox.isSelected();
            System.out.println("auto shoot: " + autoShootCheckBox.isSelected());
        });
        autoShootCheckBox.setSelected(true);
        autoShootCheckBox.setText("自动射击");
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(5, 1, 10, 80));
        innerPanel.add(easyModeButton);
        innerPanel.add(mediumModeButton);
        innerPanel.add(hardModeButton);
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.add(musicOnCheckBox);
        checkBoxPanel.add(autoShootCheckBox);
        innerPanel.add(checkBoxPanel);
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
}
