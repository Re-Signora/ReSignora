package work.chiro.game.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.scene.AbstractSceneClient;
import work.chiro.game.utils.Utils;

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
        JButton startButton = new JButton();
        startButton.addActionListener(e -> {
            RunningConfig.difficulty = Difficulty.Easy;
            nextScene(GameWindow.class);
        });
        startButton.setText("开始游戏");

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
            Utils.getLogger().info("music enable: " + musicOnCheckBox.isSelected());
        });
        musicOnCheckBox.setSelected(true);
        musicOnCheckBox.setText("打开音效");
        musicOnCheckBox.setSelected(RunningConfig.musicEnable);
        autoShootCheckBox.addActionListener(e -> {
            RunningConfig.autoShoot = autoShootCheckBox.isSelected();
            Utils.getLogger().info("auto shoot: " + autoShootCheckBox.isSelected());
        });
        autoShootCheckBox.setSelected(true);
        autoShootCheckBox.setText("自动射击");
        autoShootCheckBox.setSelected(RunningConfig.autoShoot);
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(5, 1, 10, 8));
        innerPanel.add(startButton);
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
