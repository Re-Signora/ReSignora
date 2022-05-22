package work.chiro.game.application;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import work.chiro.game.aircraft.BossEnemy;
import work.chiro.game.aircraft.BossEnemyFactory;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.compatible.XGraphics;
import work.chiro.game.compatible.XGraphicsPC;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.history.HistoryImpl;
import work.chiro.game.history.HistoryObjectFactory;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.scene.SceneRun;
import work.chiro.game.timer.Timer;
import work.chiro.game.windows.HistoryWindow;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class GamePanel extends JPanel {
    private Font myFontBase = null;
    private final HeroControllerImpl heroControllerImpl = HeroControllerImpl.getInstance(this);
    private final Game game;
    private String lastProvidedName = null;
    private String lastProvidedMessage = null;
    private final Object waitObject = new Object();
    private static double scale = 1.0;

    public static void setScale(double scale) {
        GamePanel.scale = scale;
    }

    public void resetStates() {
        heroControllerImpl.clear();
        game.resetStates();
    }

    public void action() {
        game.action();
        addEvents();
    }

    public GamePanel() {
        game = new Game(heroControllerImpl);
        loadFont();
        System.out.println("GamePanel instance created!");
        game.setOnFinish(() -> {
            System.out.println("finish!");
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
                if (RunningConfig.score > 0) {
                    HistoryImpl.getInstance().addOne(
                            new HistoryObjectFactory(
                                    name == null ? "Nanshi" : name.isEmpty() ? "Nanshi" : name,
                                    RunningConfig.score,
                                    message == null ? "NO MESSAGE" : message.isEmpty() ? "NO MESSAGE" : message,
                                    RunningConfig.difficulty)
                                    .create());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Input exception: " + e);
            } finally {
                SceneRun.getInstance().setNextScene(HistoryWindow.class);
                synchronized (waitObject) {
                    waitObject.notify();
                }
            }
        });
        game.setOnPaint(this::repaint);
        game.setOnFrame(heroControllerImpl::onFrame);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                game.resetStates();
                if (RunningConfig.allowResize) {
                    RunningConfig.windowWidth = getWidth();
                    RunningConfig.windowHeight = getHeight();
                    HeroAircraftFactory.getInstance().setPosition(RunningConfig.windowWidth / 2.0, RunningConfig.windowHeight - ImageManager.getInstance().HERO_IMAGE.getHeight());
                }
                game.action();
            }
        });
    }

    public void addEvents() {
        // 获取键盘焦点
        game.getTimerController().add(new Timer(100, this::requestFocus));
    }

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g 绘图
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        BufferedImage thisFrame = getGraphicsConfiguration().createCompatibleImage(RunningConfig.windowWidth, RunningConfig.windowHeight);
        Graphics2D graphicsNew = thisFrame.createGraphics();

        List<List<? extends AbstractFlyingObject>> allObjects = game.getAllObjects();

        XGraphics xGraphics = new XGraphicsPC() {
            @Override
            protected Graphics getGraphics() {
                return graphicsNew;
            }
        };

        // 绘制所有物体
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (allObjects) {
            allObjects.forEach(objList -> {
                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (objList) {
                    objList.forEach(obj -> obj.draw(xGraphics));
                }
            });
        }

        //绘制得分和生命值
        paintInfo(graphicsNew);

        // resize 到显示帧
        AffineTransform af = AffineTransform.getScaleInstance(scale, scale);
        Graphics2D g2d = (Graphics2D) g;
        // g.drawImage(thisFrame, 0, 0, null);
        g2d.drawRenderedImage(thisFrame, af);
        g2d.dispose();
    }

    private void paintInfo(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(0xcfcfcfcf));
        g.setFont(myFontBase);
        g.drawString("SCORE:" + (int) (RunningConfig.score), x, y);
        y = y + 20;
        g.drawString("LIFE:" + (int) (HeroAircraftFactory.getInstance().getHp()), x, y);
        y = y + 20;
        BossEnemy boss = BossEnemyFactory.getInstance();
        if (boss == null) {
            g.drawString("Before Boss:" + (int) (game.getNextBossScore() - RunningConfig.score), x, y);
        } else {
            g.drawString("BOSS LIFE:" + (int) (boss.getHp()), x, y);
        }
        y = y + 20;
        g.drawString("FPS:" + game.getTimerController().getFps(), x, y);
    }

    private void loadFont() {
        try {
            myFontBase = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/fonts/Genshin.ttf"))).deriveFont(22f);
        } catch (FontFormatException | IOException e) {
            myFontBase = new Font("SansSerif", Font.PLAIN, 22);
        }
    }

    public Object getWaitObject() {
        return waitObject;
    }
}
