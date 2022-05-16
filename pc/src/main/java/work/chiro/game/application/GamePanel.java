package work.chiro.game.application;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;

import work.chiro.game.aircraft.BossEnemy;
import work.chiro.game.aircraft.BossEnemyFactory;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.scene.SceneRun;
import work.chiro.game.thread.MusicThreadFactory;
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
    private final Object waitObject = new Object();

    public void resetStates() {
        heroControllerImpl.clear();
        game.resetStates();
    }

    public void action() {
        game.action();
        addEvents();
    }

    public GamePanel(Difficulty difficulty) {
        game = new Game(difficulty, heroControllerImpl);
        loadFont();
        System.out.println("GamePanel instance created!");
        game.setOnFinish(() -> {
            SceneRun.getInstance().setNextScene(HistoryWindow.class);
            synchronized (waitObject) {
                waitObject.notify();
            }
        });
        game.setOnPaint(this::repaint);
        game.setOnFrame(heroControllerImpl::onFrame);
    }

    public void addEvents() {
        // 获取键盘焦点
        game.getTimerController().add(new Timer(100, this::requestFocus));
    }

    private void stopAllMusic() {
        System.out.println("stopping all music");
        MusicThreadFactory.getInstance().interruptAll();
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

        // 绘制所有物体
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (allObjects) {
            allObjects.forEach(objList -> {
                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (objList) {
                    objList.forEach(obj -> obj.draw(g));
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
