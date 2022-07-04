package work.chiro.game.application;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Toolkit;
import java.util.Arrays;

import javax.swing.JFrame;

import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.compatible.ResourceProviderPC;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.config.RunningConfigPC;
import work.chiro.game.scene.Scene;
import work.chiro.game.scene.SceneRun;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.UtilsPC;
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
        ResourceProvider.setInstance(new ResourceProviderPC());
        Utils.getLogger().info("Hello Re-Signora");
//        这只是个调试语句嘛？？？

//        设置系统设置是设置什么啊~？？？硬件加速？
//        只是几条语句而已qwq   running
        if (RunningConfig.enableHardwareSpeedup) {
            System.setProperty("sun.java2d.opengl", "true");
            System.setProperty("swing.aatext", "true");
            System.setProperty("sun.java2d.ddscale", "true");
        }

        // 设置内部模式
        RunningConfig.modePC = true;

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        UtilsPC.refreshWindowSize();
//        刷新？？
        JFrame frame = new JFrame();
        frame.setSize(RunningConfigPC.displayWindowWidth, RunningConfigPC.displayWindowHeight);
        if (!RunningConfig.allowResize) {
            frame.setResizable(false);
        }


        // 设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - RunningConfigPC.displayWindowWidth) / 2,
                ((int) screenSize.getHeight() - RunningConfigPC.displayWindowHeight) / 2,
                RunningConfigPC.displayWindowWidth, RunningConfigPC.displayWindowHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 无边框
        if (RunningConfigPC.fullScreen) {
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setAlwaysOnTop(true);
            frame.setResizable(false);
            frame.getGraphicsConfiguration().getDevice()
                    .setFullScreenWindow(frame);
            frame.getGraphicsConfiguration().getDevice().getFullScreenWindow().setIgnoreRepaint(true);
            Utils.getLogger().warn("screenSize: {}", screenSize);
            frame.getGraphicsConfiguration().getDevice()
                    .setDisplayMode(new DisplayMode(
                            1920,
                            1080,
                            32,
                            60));
        } else {
            frame.setUndecorated(true);
        }

//        设置边框，画面框等

//        没看懂，启动程序在哪？？？
        try {
            SceneRun.newInstance(frame, Arrays.asList(
                            new Scene("Main Window", MainWindow.getInstance()),
                            new Scene("Game Window", GameWindow.getInstance()),
                            new Scene("History Window", HistoryWindow.getInstance())
                    ))
                    .setNextScene(MainWindow.class)
                    .run();
        } catch (SceneRun.SceneRunDoneException e) {
            Utils.getLogger().info("All scene run done.");
        }
        System.exit(0);
    }
}
