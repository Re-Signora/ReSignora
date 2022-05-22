package work.chiro.game.application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import javax.swing.JFrame;

import work.chiro.game.compatible.ResourceProvider;
import work.chiro.game.compatible.ResourceProviderPC;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.scene.Scene;
import work.chiro.game.scene.SceneRun;
import work.chiro.game.windows.GameWindow;
import work.chiro.game.windows.HistoryWindow;
import work.chiro.game.windows.MainWindow;

/**
 * 程序入口
 *
 * @author hitsz
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");

        ResourceProvider.setInstance(new ResourceProviderPC());

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        frame.setSize(RunningConfig.windowWidth, RunningConfig.windowHeight);
        if (!RunningConfig.allowResize) {
            frame.setResizable(false);
        }
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - RunningConfig.windowWidth) / 2, 0,
                RunningConfig.windowWidth, RunningConfig.windowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            SceneRun.newInstance(frame, Arrays.asList(
                            new Scene("Main Window", MainWindow.getInstance()),
                            new Scene("Game Window", GameWindow.getInstance()),
                            new Scene("History Window", HistoryWindow.getInstance())
                    ))
                    .setNextScene(MainWindow.class)
                    .run();
        } catch (SceneRun.SceneRunDoneException e) {
            System.out.println("All scene run done.");
        }
        System.exit(0);
    }
}
