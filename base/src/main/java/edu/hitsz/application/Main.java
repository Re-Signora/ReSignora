package edu.hitsz.application;

import edu.hitsz.scene.Scene;
import edu.hitsz.scene.SceneClient;
import edu.hitsz.scene.SceneRun;
import edu.hitsz.scene.AbstractSceneRunnable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * 程序入口
 *
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            try {
                new SceneRun(frame, Arrays.asList(
                        new Scene("Main Window", new AbstractSceneRunnable() {
                            MainWindow mainWindow = null;

                            @Override
                            public SceneClient getClient() {
                                if (mainWindow == null) {
                                    synchronized (MainWindow.class) {
                                        mainWindow = new MainWindow();
                                    }
                                }
                                return mainWindow;
                            }
                        }),
                        new Scene("Game Window", new AbstractSceneRunnable() {
                            @Override
                            public SceneClient getClient() {
                                return new GameWindow();
                            }
                        })
                )).run();
            } catch (SceneRun.SceneRunDoneException e) {
                System.out.println("run done.");
                break;
            }
        }
    }
}
