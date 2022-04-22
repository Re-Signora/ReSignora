package edu.hitsz.application;

import edu.hitsz.scene.Scene;
import edu.hitsz.scene.SceneClient;
import edu.hitsz.scene.SceneRun;
import edu.hitsz.scene.SceneRunnable;

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

        // Game game = new Game();
        // frame.add(game);
        // frame.setContentPane(new MainWindow().getMainPanel());
        // frame.setVisible(true);
        // game.action();

        new SceneRun(frame, Arrays.asList(
                new Scene("Main Window", new SceneRunnable() {
                    MainWindow mainWindow = null;
                    @Override
                    public SceneClient getClient() {
                        mainWindow = new MainWindow();
                        return mainWindow;
                    }

                    @Override
                    public void run() {
                        synchronized (this) {
                            System.out.println("main window in");
                            try {
                                mainWindow.getWaitObject().wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("main window out");
                            this.notify();
                        }
                    }
                })
        )).run();
    }
}
