package work.chiro.game.application;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import work.chiro.game.aircraft.HeroAircraft;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.windows.GameWindow;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroControllerImpl implements HeroController {
    final static double MOVE_SPEED = 1;

    static public class KeyCode {
        public final static int UP = KeyEvent.VK_W;
        public final static int DOWN = KeyEvent.VK_S;
        public final static int LEFT = KeyEvent.VK_A;
        public final static int RIGHT = KeyEvent.VK_D;
        public final static int QUIT = KeyEvent.VK_ENTER;
    }

    final private Set<Integer> pressedKeys = new HashSet<>();
    private static HeroControllerImpl instance = null;
    private Double lastFrameTime = null;

    public HeroControllerImpl(GamePanel game) {
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        };
        game.addKeyListener(keyAdapter);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                // Utils.getLogger().info("mouseDragged!");
                HeroAircraft heroAircraft = HeroAircraftFactory.getInstance();
                if (heroAircraft != null) {
                    heroAircraft.setPosition(new Vec2().fromVector(new Vec2(Utils.setInRange(e.getX(), 0, RunningConfig.windowWidth), Utils.setInRange(e.getY(), 0, RunningConfig.windowHeight)).divide(GamePanel.getScale())));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Utils.getLogger().info("mousePressed! {}", e);
                game.requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Utils.getLogger().info("mouseReleased! {}", e);
                GameWindow.getInstance().getGamePanel().getGame().getLayout().actionPointerRelease(
                        new Vec2(e.getX(), e.getY())
                );
            }
        };

        game.addMouseListener(mouseAdapter);
        game.addMouseMotionListener(mouseAdapter);
        game.requestFocus();
    }

    public Boolean pressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public void onFrame() {
        if (lastFrameTime == null) {
            lastFrameTime = Utils.getTimeMills();
        }
        if (HeroAircraftFactory.getInstance() == null) {
            return;
        }
        double now = Utils.getTimeMills();
        double frameTime = now - lastFrameTime;
        Vec2 next = new Vec2();
        double scale = 0.26;
        for (int pressedKey : pressedKeys) {
            switch (pressedKey) {
                case KeyCode.UP:
                    next.setY(-scale);
                    break;
                case KeyCode.DOWN:
                    next.setY(scale);
                    break;
                case KeyCode.LEFT:
                    next.setX(-scale);
                    break;
                case KeyCode.RIGHT:
                    next.setX(scale);
                    break;
                case KeyCode.QUIT:
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
        Vec2 nextScaled = next.fromVector(next.times(frameTime * MOVE_SPEED));
        Vec2 newPos = HeroAircraftFactory.getInstance().getPosition().plus(nextScaled);
        HeroAircraftFactory.getInstance().setPosition(
                Utils.setInRange(newPos.getX(), 0, RunningConfig.windowWidth),
                Utils.setInRange(newPos.getY(), 0, RunningConfig.windowHeight - HeroAircraftFactory.getInstance().getHeight() / 2)
        );
        lastFrameTime = now;
    }

    static public HeroControllerImpl getInstance(GamePanel game) {
        if (instance == null) {
            synchronized (HeroControllerImpl.class) {
                instance = new HeroControllerImpl(game);
            }
        }
        return instance;
    }

    public void clear() {
        lastFrameTime = null;
        pressedKeys.clear();
    }

    public boolean isShootPressed() {
        return true;
    }
}
