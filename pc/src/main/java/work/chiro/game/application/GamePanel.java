package work.chiro.game.application;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.VolatileImage;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import work.chiro.game.compatible.XGraphicsPC;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.config.RunningConfigPC;
import work.chiro.game.game.Game;
import work.chiro.game.scene.SceneRun;
import work.chiro.game.storage.history.HistoryImpl;
import work.chiro.game.storage.history.HistoryObjectFactory;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.UtilsPC;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.windows.HistoryWindow;
import work.chiro.game.windows.MainWindow;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XGraphics;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class GamePanel extends JPanel {
    private final CharacterControllerPCImpl heroControllerImpl = CharacterControllerPCImpl.getInstance(this);
    private String lastProvidedName = null;
    private String lastProvidedMessage = null;
    private final Object waitObject = new Object();
    private static double scale = 1.0;
    private static boolean justResized = false;


//    面板设置？
    public static void setScale(double scale) {
        GamePanel.scale = scale;
    }

    public static double getScale() {
        return scale;
    }

    public static boolean getJustResized() {
        return GamePanel.justResized;
    }

    public void resetStates() {
        heroControllerImpl.clear();
        if (Game.getInstance() == null) {
            Game.createInstance(heroControllerImpl);
        }
        // Game.getInstance().resetStates();
    }

    public void action() {
        if (RunningConfig.modePC) RunningConfig.targetServerHost = null;
        if (Game.getInstance() == null) {
            Game.createInstance(heroControllerImpl);
        }
//        开启英雄控制？
//        开始操作？？？？这是啥玩意~？
        Game.getInstance().action();
        addTimers();
    }

    private Graphics2D getGraphics2D() {
        return (Graphics2D) getGraphics();
    }

    public GamePanel() {
//        这句话没有看懂
        ResourceProvider.getInstance().setXGraphicsGetter(() -> new XGraphicsPC() {
//            XGraphicsPC()这东西没有初始化的嘛qwq
//            c，居然还能这样重定义 ，绝了
            @Override
            protected Graphics2D getGraphics() {
                return getGraphics2D();
            }

            @Override
            protected GraphicsConfiguration getXGraphicsConfiguration() {
                return getGraphicsConfiguration();
            }
        });
//        就是从这个地方开始游戏了呗qwq
        Game.createInstance(heroControllerImpl);

        // Game.getInstance().setOnExit(() -> window.nextScene(MainWindow.class));
        Game.getInstance().setOnExit(() -> {
            SceneRun.getInstance().setNextScene(MainWindow.class);
            synchronized (waitObject) {
                waitObject.notify();
            }
        });
        Utils.getLogger().info("GamePanel instance created!");
//        这玩意儿是不是应该删掉啊qwq？？？？
        Game.getInstance().setOnFinish(() -> {
            Utils.getLogger().info("finish!");
            if (RunningConfig.score > 0) {
                try {
                    String name = JOptionPane.showInputDialog("输入你的名字", lastProvidedName == null ? "Nanshi" : lastProvidedName);
                    if (name == null) {
                        String name2 = JOptionPane.showInputDialog("输入你的名字", lastProvidedName == null ? "Nanshi" : lastProvidedName);
                        if (name2 == null) {
                            int res = JOptionPane.showConfirmDialog(null, "不保存记录?", "Save Game", JOptionPane.OK_CANCEL_OPTION);
                            if (res == JOptionPane.YES_OPTION) {
                                throw new Exception();
                            }
                        } else {
                            lastProvidedName = name2;
                        }
                    } else {
                        lastProvidedName = name;
                    }
                    String message = JOptionPane.showInputDialog("输入额外的信息", lastProvidedMessage == null ? "NO MESSAGE" : lastProvidedMessage);
                    lastProvidedMessage = message;
                    // 保存游戏结果
                    HistoryImpl.getInstance().addOne(
                            new HistoryObjectFactory(
                                    name == null ? "Nanshi" : name.isEmpty() ? "Nanshi" : name,
                                    RunningConfig.score,
                                    message == null ? "NO MESSAGE" : message.isEmpty() ? "NO MESSAGE" : message,
                                    RunningConfig.difficulty)
                                    .create());
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.getLogger().warn("Input exception: " + e);
                } finally {
                    SceneRun.getInstance().setNextScene(HistoryWindow.class);
                    synchronized (waitObject) {
                        waitObject.notify();
                    }
                }
            }
        });
//        ？？？？一直很想问qwq  ：：是啥意思啊
        Game.getInstance().setOnPaint(this::repaint);
        Game.getInstance().setOnFrame(heroControllerImpl::onFrame);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                // if (RunningConfig.allowResize) {
                UtilsPC.refreshWindowSize(getWidth(), getHeight());
                justResized = true;
//                调整大小完成，但是为什么要记录呢qwq，不理解？？？？
                // }
            }
        });
    }

    public void addTimers() {
        // 获取键盘焦点
        Game.getInstance().getTimerController().add(this, new Timer(100, (controller, timer) -> this.requestFocus()));
    }

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g 绘图
     */
    @Override
    public void paint(Graphics g) {
        if (Game.getInstance() == null) return;
        double timeStart = TimeManager.getTimeMills();
        VolatileImage thisFrame = getGraphicsConfiguration().createCompatibleVolatileImage(RunningConfigPC.displayWindowWidth, RunningConfigPC.displayWindowHeight);
        Graphics2D graphicsNew = thisFrame.createGraphics();

        XGraphics xGraphics = new XGraphicsPC() {
            @Override
            protected Graphics2D getGraphics() {
                return graphicsNew;
            }

            @Override
            protected GraphicsConfiguration getXGraphicsConfiguration() {
                return getGraphicsConfiguration();
            }
        };

        xGraphics.paintInOrdered(Game.getInstance());

        double timePaint = TimeManager.getTimeMills();

        //绘制得分和生命值
        xGraphics.paintInfo(Game.getInstance());

        double timePaintInfo = TimeManager.getTimeMills();

        // resize 到显示帧
        g.drawImage(thisFrame, 0, 0, null);
        double timeResize = TimeManager.getTimeMills();
        graphicsNew.dispose();
        Utils.getLogger().debug("paint -- object: {}, info: {}, resize: {}", timePaint - timeStart, timePaintInfo - timePaint, timeResize - timePaintInfo);

        justResized = false;
    }

    public Object getWaitObject() {
        return waitObject;
    }
}
