package work.chiro.game.application;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.ObjectController;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class ObjectControllerImpl extends ObjectController {
    static public class KeyCode {
        public final static int UP = KeyEvent.VK_W;
        public final static int DOWN = KeyEvent.VK_S;
        public final static int LEFT = KeyEvent.VK_A;
        public final static int RIGHT = KeyEvent.VK_D;
        public final static int QUIT = KeyEvent.VK_ENTER;
    }

    final private Set<Integer> pressedKeys = new HashSet<>();
    private static ObjectControllerImpl instance = null;

    private Vec2 getScaledPosition(MouseEvent e) {
        return new Vec2().fromVector(new Vec2(e.getX(), e.getY()).divide(GamePanel.getScale()));
    }

    public ObjectControllerImpl(GamePanel game) {
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
                if (getTarget() != null) {
                    getTarget().setPosition(new Vec2().fromVector(new Vec2(
                            Utils.setInRange(e.getX(), 0, RunningConfig.windowWidth),
                            Utils.setInRange(e.getY(), 0, RunningConfig.windowHeight)
                    ).divide(GamePanel.getScale())));
                }
                Game.getInstance().getTopActivity().actionPointerDragged(List.of(getScaledPosition(e)));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Utils.getLogger().debug("mousePressed! {}", e);
                game.requestFocus();
                Game.getInstance().getTopActivity().actionPointerPressed(getScaledPosition(e));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Utils.getLogger().debug("mouseReleased! {}", e);
                Game.getInstance().getTopActivity().actionPointerRelease(getScaledPosition(e));
            }
        };

        game.addMouseListener(mouseAdapter);
        game.addMouseMotionListener(mouseAdapter);
        game.requestFocus();
    }

    public Boolean pressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    @Override
    public void onFrame() {
        if (lastFrameTime == null) {
            lastFrameTime = Utils.getTimeMills();
        }
        if (getTarget() == null) {
            return;
        }
        double now = Utils.getTimeMills();
        double frameTime = now - lastFrameTime;
        Vec2 next = new Vec2();
        for (int pressedKey : pressedKeys) {
            switch (pressedKey) {
                case KeyCode.UP:
                    next.setY(-movingScale);
                    break;
                case KeyCode.DOWN:
                    next.setY(movingScale);
                    break;
                case KeyCode.LEFT:
                    next.setX(-movingScale);
                    break;
                case KeyCode.RIGHT:
                    next.setX(movingScale);
                    break;
                case KeyCode.QUIT:
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
        Vec2 newPos = getTarget().getPosition().plus(next.fromVector(next.times(frameTime * MOVE_SPEED)));
        setTargetPosition(newPos);
        lastFrameTime = now;
    }

    static public ObjectControllerImpl getInstance(GamePanel game) {
        if (instance == null) {
            synchronized (ObjectControllerImpl.class) {
                instance = new ObjectControllerImpl(game);
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
