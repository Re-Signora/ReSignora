package work.chiro.game.application;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
import work.chiro.game.compatible.XImage;
import work.chiro.game.compatible.XImageFactory;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.dao.HistoryImpl;
import work.chiro.game.dao.HistoryObjectFactory;
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
                RunningConfig.windowWidth = getWidth();
                RunningConfig.windowHeight = getHeight();
                HeroAircraftFactory.getInstance().setPosition(RunningConfig.windowWidth / 2.0, RunningConfig.windowHeight - ImageManager.getInstance().HERO_IMAGE.getHeight());
                game.action();
            }
        });
    }

    public void addEvents() {
        // 获取键盘焦点
        game.getTimerController().add(new Timer(100, this::requestFocus));
    }

    private abstract static class XGraphicsPart implements XGraphics {
        double alpha = 1.0;
        double rotation = 0.0;
        int color = 0x0;

        @Override
        public XImage<?> drawImage(XImage<?> image, double x, double y) {
            AffineTransform af = AffineTransform.getTranslateInstance(x, y);
            af.rotate(rotation, image.getWidth() * 1.0 / 2, image.getHeight() * 1.0 / 2);
            Graphics2D graphics2D = (Graphics2D) (getGraphics());
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha));
            graphics2D.drawImage((Image) image.getImage(), af, null);
            return image;
        }

        @Override
        public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h) {
            if (image.getWidth() != (int) w || image.getHeight() != (int) h) {
                BufferedImage raw = (BufferedImage) image.getImage();
                Image resizedImage = raw.getScaledInstance((int) w, (int) h, Image.SCALE_DEFAULT);
                BufferedImage bufferedImage = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g = bufferedImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(resizedImage, 0, 0, (int) w, (int) h, null);
                g.dispose();
                return drawImage(new XImageFactory().create(bufferedImage), x, y);
            } else {
                return drawImage(image, x, y);
            }
        }

        @Override
        public XGraphics setAlpha(double alpha) {
            this.alpha = alpha;
            return this;
        }

        @Override
        public XGraphics setRotation(double rotation) {
            this.rotation = rotation;
            return this;
        }

        @Override
        public XGraphics setColor(int color) {
            this.color = color;
            return this;
        }

        @Override
        public XGraphics fillRect(double x, double y, double width, double height) {
            getGraphics().setColor(new Color(color));
            getGraphics().fillRect((int) x, (int) y, (int) width, (int) height);
            return this;
        }

        @Override
        public XGraphics drawString(String text, double x, double y) {
            getGraphics().drawString(text, (int) x, (int) y);
            return this;
        }

        abstract protected Graphics getGraphics();
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

        List<List<? extends AbstractFlyingObject>> allObjects = game.getAllObjects();

        XGraphics xGraphics = new XGraphicsPart() {
            @Override
            protected Graphics getGraphics() {
                return g;
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
        paintInfo(g);
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
