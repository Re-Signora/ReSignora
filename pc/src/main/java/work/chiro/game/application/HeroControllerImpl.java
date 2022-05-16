package work.chiro.game.application;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import work.chiro.game.aircraft.HeroAircraft;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.config.Constants;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroControllerImpl implements HeroController {
    final static double MOVE_SPEED = 1;

    static public class KeyCode {
        public final static int UP = 38;
        public final static int DOWN = 40;
        public final static int LEFT = 37;
        public final static int RIGHT = 39;
        public final static int SLOW = 16;
        public final static int BUFF = 88;
        public final static int SHOOT = 90;
        public final static int QUIT = 81;
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
                HeroAircraft heroAircraft = HeroAircraftFactory.getInstance();
                if (heroAircraft != null) {
                    heroAircraft.setPosition(Utils.setInRange(e.getX(), 0, Constants.WINDOW_WIDTH), Utils.setInRange(e.getY(), 0, Constants.WINDOW_HEIGHT));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                game.requestFocus();
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
        double scaleFast = 0.26;
        double scaleSlow = 0.1;
        double scale = pressed(KeyCode.SLOW) ? scaleSlow : scaleFast;
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
                Utils.setInRange(newPos.getX(), 0, Constants.WINDOW_WIDTH),
                Utils.setInRange(newPos.getY(), 0, Constants.WINDOW_HEIGHT - HeroAircraftFactory.getInstance().getHeight() / 2)
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
        return pressed(KeyCode.SHOOT);
    }
}
