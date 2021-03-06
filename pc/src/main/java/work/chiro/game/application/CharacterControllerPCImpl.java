package work.chiro.game.application;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.config.RunningConfigPC;
import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.CharacterController;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class CharacterControllerPCImpl extends CharacterController {
    static public class KeyCode {
        public final static int UP = KeyEvent.VK_W;
        public final static int DOWN = KeyEvent.VK_S;
        public final static int LEFT = KeyEvent.VK_A;
        public final static int RIGHT = KeyEvent.VK_D;
        public final static int SKILL = KeyEvent.VK_E;
        public final static int CHARGED = KeyEvent.VK_Q;
        public final static int CHARACTER_1 = KeyEvent.VK_1;
        public final static int CHARACTER_2 = KeyEvent.VK_2;
        public final static int CHARACTER_3 = KeyEvent.VK_3;
        public final static int CHARACTER_4 = KeyEvent.VK_4;
        public final static int QUIT = KeyEvent.VK_ENTER;
        public final static int BACK = KeyEvent.VK_ESCAPE;
    }

    final private Set<Integer> pressedKeys = new HashSet<>();
    private static CharacterControllerPCImpl instance = null;

    private Vec2 getScaledPosition(MouseEvent e) {
        return new Vec2().fromVector(new Vec2(e.getX(), e.getY()).divide(GamePanel.getScale()));
    }

    public CharacterControllerPCImpl(GamePanel game) {
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!pressedKeys.contains(e.getKeyCode())) {
                    Utils.getLogger().debug("key down: {}", e.getKeyChar());
                    onKeyDown(e.getKeyCode());
                }
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Utils.getLogger().debug("key up: {}", e.getKeyChar());
                onKeyUp(e.getKeyCode());
                pressedKeys.remove(e.getKeyCode());
            }
        };
        game.addKeyListener(keyAdapter);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (RunningConfigPC.dragControlObject && getTarget() != null && !TimeManager.isPaused()) {
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

    protected void onKeyDown(int code) {
        // 需要控制 Object 的部分
        if (getBattleActivity() != null) {
            switch (code) {
                case KeyCode.SKILL:
                    getBattleActivity().beforeSkillAttack();
                    break;
                case KeyCode.CHARGED:
                    getBattleActivity().beforeChargedAttack();
                default:
                    break;
            }
        }
    }

    protected void onKeyUp(int code) {
        // 不需要控制 Object 的部分
        switch (code) {
            case KeyCode.QUIT:
                System.exit(0);
                break;
            case KeyCode.BACK:
                if (Game.getInstance() != null && Game.getInstance().getActivityManager().hasTop()) {
                    Game.getInstance().getActivityManager().finishActivity(Game.getInstance().getTopActivity());
                }
                break;
            default:
                break;
        }
        // 需要控制 Object 的部分
        if (getBattleActivity() == null) return;
        switch (code) {
            case KeyCode.SKILL:
                getBattleActivity().skillAttack();
                break;
            case KeyCode.CHARGED:
                getBattleActivity().chargedAttack();
            default:
                break;
        }
    }

    @Override
    public void onFrame() {
        if (lastFrameTime == null) {
            lastFrameTime = TimeManager.getTimeMills();
        }
        if (getTarget() == null) return;
        if (TimeManager.isPaused()) return;
        double now = TimeManager.getTimeMills();
        double frameTime = now - lastFrameTime;
        Vec2 next = new Vec2();
        for (int pressedKey : pressedKeys) {
            switch (pressedKey) {
                case KeyCode.UP:
                    next.setY(next.getY() - movingScale);
                    break;
                case KeyCode.DOWN:
                    next.setY(next.getY() + movingScale);
                    break;
                case KeyCode.LEFT:
                    next.setX(next.getX() - movingScale);
                    break;
                case KeyCode.RIGHT:
                    next.setX(next.getX() + movingScale);
                    break;
                default:
                    break;
            }
        }
        Vec2 newPos = getTarget().getPosition().plus(next.fromVector(next.times(frameTime * MOVE_SPEED)));
        setTargetPosition(newPos);
        lastFrameTime = now;
    }

    static public CharacterControllerPCImpl getInstance(GamePanel game) {
        if (instance == null) {
            synchronized (CharacterControllerPCImpl.class) {
                instance = new CharacterControllerPCImpl(game);
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
