package work.chiro.game.game;

import work.chiro.game.aircraft.*;
import work.chiro.game.application.HeroController;
import work.chiro.game.application.MusicManager;
import work.chiro.game.application.MusicThreadFactory;
import work.chiro.game.application.MyThreadFactory;
import work.chiro.game.background.*;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.bullet.BaseBullet;
import work.chiro.game.config.*;
import work.chiro.game.dao.HistoryImpl;
import work.chiro.game.dao.HistoryObjectFactory;
import work.chiro.game.prop.AbstractProp;
import work.chiro.game.scene.SceneRun;
import work.chiro.game.timer.Timer;
import work.chiro.game.timer.TimerController;
import work.chiro.game.utils.Utils;
import work.chiro.game.windows.HistoryWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {
    /**
     * 创建线程的工厂函数
     */
    static private final MyThreadFactory THREAD_FACTORY = new MyThreadFactory("AircraftWar");
    static private final MusicThreadFactory MUSIC_FACTORY = new MusicThreadFactory("AircraftWar-Music");
    /**
     * 线程池，自动管理
     */
    @SuppressWarnings("AlibabaThreadPoolCreation")
    static private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Constants.GAME_POOL_SIZE, THREAD_FACTORY);

    public static MyThreadFactory getThreadFactory() {
        return THREAD_FACTORY;
    }

    public static MusicThreadFactory getMusicFactory() {
        return MUSIC_FACTORY;
    }

    private HeroAircraft heroAircraft;
    private final List<AbstractAircraft> heroAircrafts = new LinkedList<>();
    private final List<AbstractBackground> backgrounds = new LinkedList<>();
    private final List<BossEnemy> bossAircrafts = new LinkedList<>();
    private final List<AbstractAircraft> enemyAircrafts = new LinkedList<>();
    private final List<BaseBullet> heroBullets = new LinkedList<>();
    private final List<BaseBullet> enemyBullets = new LinkedList<>();
    private final List<AbstractProp> props = new LinkedList<>();
    private final List<List<? extends AbstractFlyingObject>> allObjects = Arrays.asList(
            backgrounds, heroBullets, heroAircrafts, enemyBullets, enemyAircrafts, bossAircrafts, props
    );
    private boolean gameOverFlag = false;
    private boolean startedFlag = false;
    private double score = 0;
    private double nextBossScore;
    private final Object waitObject = new Object();
    private Future<?> future = null;
    private Font myFontBase = null;
    private String lastProvidedName = null;
    private String lastProvidedMessage = null;
    private final HeroController heroController;
    private final TimerController timerController = new TimerController();
    private AbstractConfig config;
    private Difficulty difficulty;

    public void resetStates() {
        gameOverFlag = false;
        score = 0;
        enemyBullets.clear();
        enemyAircrafts.clear();
        props.clear();
        difficulty = RunningConfig.difficulty;
        config = new ConfigFactory(difficulty).create();
        heroAircraft = new HeroAircraftFactory().clearInstance().create(config);
        heroAircrafts.clear();
        heroAircrafts.add(heroAircraft);
        bossAircrafts.clear();
        BossEnemyFactory.clearInstance();
        timerController.clear();
        heroController.clear();

        flushBackground();
    }

    public boolean getGameOverFlag() {
        return gameOverFlag;
    }

    public boolean getStartedFlag() {
        return startedFlag;
    }

    public Object getWaitObject() {
        return waitObject;
    }

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    public Game(Difficulty difficulty) {
        this.difficulty = difficulty;
        config = new ConfigFactory(difficulty).create();
        nextBossScore = score + config.getBossScoreThreshold().getScaleNow().getX();
        heroAircraft = new HeroAircraftFactory().create(config);
        heroAircrafts.add(heroAircraft);
        loadFont();
        flushBackground();
        // 启动英雄机鼠标监听
        heroController = HeroController.getInstance(this);
        System.out.println("Game instance created!");
    }

    private void flushBackground() {
        backgrounds.clear();
        switch (difficulty) {
            case Easy:
                backgrounds.add(new OtherBackgroundFactory<>(new EasyBackground()).create());
                break;
            case Medium:
                backgrounds.add(new OtherBackgroundFactory<>(new MediumBackground()).create());
                break;
            case Hard:
                backgrounds.add(new OtherBackgroundFactory<>(new HardBackground()).create());
                break;
            default:
                break;
        }
    }

    private void heroShoot() {
        synchronized (heroBullets) {
            heroBullets.addAll(heroAircraft.shoot(List.of(enemyAircrafts, bossAircrafts)));
        }
    }

    public void addEvents() {
        MusicManager.initAll();
        timerController.init(Utils.getTimeMills());
        // 英雄射击事件
        timerController.add(new Timer(config.getHeroShoot(), () -> {
            if (RunningConfig.autoShoot) {
                heroShoot();
            } else {
                if (HeroController.getInstance(this).pressed(HeroController.KeyCode.SHOOT)) {
                    heroShoot();
                }
            }
        }));
        // 产生精英敌机事件
        timerController.add(new Timer(config.getEliteCreate(), () -> {
            synchronized (enemyAircrafts) {
                enemyAircrafts.add(new EliteEnemyFactory().create(config));
            }
        }));
        // 产生普通敌机事件
        timerController.add(new Timer(config.getMobCreate(), () -> {
            synchronized (enemyAircrafts) {
                enemyAircrafts.add(new MobEnemyFactory().create(config));
            }
        }));
        // 敌机射击事件
        timerController.add(new Timer(config.getEnemyShoot(), () -> {
            synchronized (enemyBullets) {
                enemyAircrafts.forEach(enemyAircraft -> enemyBullets.addAll(enemyAircraft.shoot()));
            }
        }));
        // boss射击事件
        timerController.add(new Timer(config.getBossShoot(), () -> {
            synchronized (enemyBullets) {
                bossAircrafts.forEach(bossEnemy -> enemyBullets.addAll(bossEnemy.shoot()));
            }
        }));
        // boss 生成事件
        timerController.add(new Timer(10, () -> {
            config.getBossScoreThreshold().update(Utils.getTimeMills());
            if (score > nextBossScore && bossAircrafts.isEmpty()) {
                synchronized (bossAircrafts) {
                    bossAircrafts.add(new BossEnemyFactory(() -> {
                        nextBossScore = config.getBossScoreThreshold().getScaleNow().getX() + score;
                        BossEnemyFactory.clearInstance();
                    }).create(config));
                }
            }
        }));
        // 获取键盘焦点
        timerController.add(new Timer(100, this::requestFocus));
        // 输出当前 config
        if (RunningConfig.difficulty != Difficulty.Easy) {
            timerController.add(new Timer(2000, () -> config.printNow()));
        } else {
            System.out.println("简单模式 Config 将不会改变: " + config);
        }
    }

    private void stopAllMusic() {
        System.out.println("stopping all music");
        getMusicFactory().interruptAll();
    }

    private void onGameOver() {
        // 游戏结束
        if (future != null) {
            future.cancel(true);
        }
        // 游戏结束
        gameOverFlag = true;
        Utils.startMusic(MusicManager.MusicType.GAME_OVER, true);
        stopAllMusic();
        System.out.println("Game Over!");
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
            if (score > 0) {
                HistoryImpl.getInstance().addOne(
                        new HistoryObjectFactory(
                                name == null ? "Nanshi" : name.isEmpty() ? "Nanshi" : name,
                                score,
                                message == null ? "NO MESSAGE" : message.isEmpty() ? "NO MESSAGE" : message,
                                difficulty)
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
    }

    protected Runnable getMainTask() {
        return () -> {
            try {
                timerController.update();
                // execute all
                timerController.execute();
                // 按键处理
                heroController.onFrame();
                // 所有物体移动
                synchronized (allObjects) {
                    allObjects.forEach(objList -> objList.forEach(AbstractFlyingObject::forward));
                }
                // 撞击检测
                crashCheckAction();
                // 后处理
                synchronized (allObjects) {
                    allObjects.forEach(objList -> objList.removeIf(AbstractFlyingObject::notValid));
                }
                // 每个时刻重绘界面
                repaint();
                // 游戏结束检查和处理
                if (heroAircraft.getHp() <= 0 && !Constants.DEBUG_NO_DEATH) {
                    onGameOver();
                }
                timerController.done();
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("this thread will exit: " + e);
            }
        };
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        startedFlag = true;
        addEvents();
        Utils.startLoopMusic(MusicManager.MusicType.BGM);
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable mainTask = getMainTask();
        int timeInterval = 1;
        // 以固定延迟时间进行执行本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
        future = executorService.scheduleWithFixedDelay(mainTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }

    private void onEnemyAircraftHit(AbstractAircraft enemyAircraft, BaseBullet bullet) {
        enemyAircraft.decreaseHp(bullet.getPower());
        bullet.vanish();
        if (enemyAircraft.notValid()) {
            score += enemyAircraft.getScore();
            props.addAll(enemyAircraft.dropProps().stream().map(
                    prop -> prop.subscribeEnemyAircrafts(enemyAircrafts)
                            .subscribeEnemyBullets(enemyBullets)
            ).collect(Collectors.toList()));
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        BossEnemy boss = BossEnemyFactory.getInstance();
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (boss != null && !boss.notValid()) {
                if (bullet.crash(boss)) {
                    onEnemyAircraftHit(boss, bullet);
                    continue;
                }
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    onEnemyAircraftHit(enemyAircraft, bullet);
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 我方获得道具，道具生效
        for (AbstractProp prop : props) {
            if (prop.notValid()) {
                continue;
            }
            if (prop.crash(heroAircraft)) {
                prop.update();
                prop.vanish();
            }
        }

        // 英雄与 Boss 相撞
        if (boss != null && !boss.notValid() && heroAircraft.crash(boss)) {
            heroAircraft.decreaseHp(Integer.MAX_VALUE);
        }
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

        // 绘制所有物体
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
        g.drawString("SCORE:" + (int) (this.score), x, y);
        y = y + 20;
        g.drawString("LIFE:" + (int) (this.heroAircraft.getHp()), x, y);
        y = y + 20;
        BossEnemy boss = BossEnemyFactory.getInstance();
        if (boss == null) {
            g.drawString("Before Boss:" + (int) (nextBossScore - score), x, y);
        } else {
            g.drawString("BOSS LIFE:" + (int) (boss.getHp()), x, y);
        }
        y = y + 20;
        g.drawString("FPS:" + timerController.getFps(), x, y);
    }

    private void loadFont() {
        try {
            myFontBase = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/fonts/Genshin.ttf"))).deriveFont(22f);
        } catch (FontFormatException | IOException e) {
            myFontBase = new Font("SansSerif", Font.PLAIN, 22);
        }
    }

    public void increaseScore(double increase) {
        score += increase;
    }
}
