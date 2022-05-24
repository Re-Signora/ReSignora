package work.chiro.game.game;

import static work.chiro.game.config.Difficulty.Easy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import work.chiro.game.config.ConfigFactory;
import work.chiro.game.config.Constants;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.objects.AbstractObject;
import work.chiro.game.objects.aircraft.AbstractAircraft;
import work.chiro.game.objects.aircraft.BossEnemy;
import work.chiro.game.objects.aircraft.BossEnemyFactory;
import work.chiro.game.objects.aircraft.EliteEnemyFactory;
import work.chiro.game.objects.aircraft.HeroAircraft;
import work.chiro.game.objects.aircraft.HeroAircraftFactory;
import work.chiro.game.objects.aircraft.MobEnemyFactory;
import work.chiro.game.objects.background.AbstractBackground;
import work.chiro.game.objects.background.BasicBackgroundFactory;
import work.chiro.game.objects.bullet.BaseBullet;
import work.chiro.game.objects.prop.AbstractProp;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.callback.BasicCallback;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.utils.timer.TimerController;
import work.chiro.game.x.activity.XActivityManager;
import work.chiro.game.x.compatible.HeroController;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.ui.XLayout;
import work.chiro.game.x.ui.XLayoutManager;
import work.chiro.game.xactivity.HomeActivity;

public class Game {
    /**
     * 线程池，自动管理
     */
    @SuppressWarnings("AlibabaThreadPoolCreation")
    static private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Constants.GAME_POOL_SIZE, MyThreadFactory.getInstance());

    private HeroAircraft heroAircraft;
    private final List<AbstractAircraft> heroAircrafts = new LinkedList<>();
    private final List<AbstractBackground> backgrounds = new LinkedList<>();
    private final List<BossEnemy> bossAircrafts = new LinkedList<>();
    private final List<AbstractAircraft> enemyAircrafts = new LinkedList<>();
    private final List<BaseBullet> heroBullets = new LinkedList<>();
    private final List<BaseBullet> enemyBullets = new LinkedList<>();
    private final List<AbstractProp> props = new LinkedList<>();
    private final XLayout layout = new XLayout();
    private final List<List<? extends AbstractFlyingObject>> allFlyingObjects = Arrays.asList(
            heroBullets, heroAircrafts, enemyBullets, enemyAircrafts, bossAircrafts, props
    );
    private boolean gameOverFlag = false;
    private double nextBossScore;
    private Future<?> future = null;
    private final TimerController timerController = new TimerController();
    private BasicCallback onFinish = null;
    private BasicCallback onPaint = null;
    private BasicCallback onFrame = null;
    private final HeroController heroController;
    private final XActivityManager activityManager = new XActivityManager(this);

    public XActivityManager getActivityManager() {
        return activityManager;
    }

    public XLayoutManager getLayoutManager() {
        return activityManager.getLayoutManager();
    }

    public void clearFlyingObjects() {
        enemyBullets.clear();
        enemyAircrafts.clear();
        props.clear();
        heroAircraft = new HeroAircraftFactory().clearInstance().create();
        heroAircrafts.clear();
        heroAircrafts.add(heroAircraft);
        bossAircrafts.clear();
        BossEnemyFactory.clearInstance();
        timerController.clear();
    }

    public void resetStates() {
        gameOverFlag = false;
        RunningConfig.score = 0;
        RunningConfig.config = new ConfigFactory(RunningConfig.difficulty).create();
        clearFlyingObjects();
        nextBossScore = RunningConfig.score + RunningConfig.config.getBossScoreThreshold().getScaleNow().getX();
    }

    public Game(HeroController heroController) {
        this.heroController = heroController;
        RunningConfig.config = new ConfigFactory(RunningConfig.difficulty).create();
        nextBossScore = RunningConfig.score + RunningConfig.config.getBossScoreThreshold().getScaleNow().getX();
        heroAircraft = new HeroAircraftFactory().create();
        heroAircrafts.add(heroAircraft);
        activityManager.setOnSwitchActivity(newActivity -> {
            if (newActivity == null) return;
            if (newActivity.getLayout() == null) return;
            Utils.getLogger().info("setOnSwitchActivity({})", newActivity);
            layout.replaceLayout(newActivity.getLayout());
            backgrounds.clear();
            backgrounds.add(new BasicBackgroundFactory(layout.getBackground()).create());
        });
        activityManager.startActivityWithBundle(HomeActivity.class);
        Utils.getLogger().info("Game instance created!");
    }

    public List<AbstractBackground> getBackgrounds() {
        return backgrounds;
    }

    private void heroShoot() {
        synchronized (heroBullets) {
            heroBullets.addAll(heroAircraft.shoot(List.of(enemyAircrafts, bossAircrafts)));
        }
    }

    public void setOnFinish(BasicCallback onFinish) {
        this.onFinish = onFinish;
    }

    public void setOnPaint(BasicCallback onPaint) {
        this.onPaint = onPaint;
    }

    public void setOnFrame(BasicCallback onFrame) {
        this.onFrame = onFrame;
    }

    public void addTimers() {
        timerController.init(Utils.getTimeMills());
        // 英雄射击事件
        timerController.add(new Timer(RunningConfig.config.getHeroShoot(), () -> {
            if (RunningConfig.autoShoot) {
                heroShoot();
            } else {
                if (heroController.isShootPressed()) {
                    heroShoot();
                }
            }
        }));
        // 产生精英敌机事件
        timerController.add(new Timer(RunningConfig.config.getEliteCreate(), () -> {
            synchronized (enemyAircrafts) {
                enemyAircrafts.add(new EliteEnemyFactory().create());
            }
        }));
        // 产生普通敌机事件
        timerController.add(new Timer(RunningConfig.config.getMobCreate(), () -> {
            synchronized (enemyAircrafts) {
                enemyAircrafts.add(new MobEnemyFactory().create());
            }
        }));
        // 敌机射击事件
        timerController.add(new Timer(RunningConfig.config.getEnemyShoot(), () -> {
            synchronized (enemyBullets) {
                enemyAircrafts.forEach(enemyAircraft -> enemyBullets.addAll(enemyAircraft.shoot()));
            }
        }));
        // boss射击事件
        timerController.add(new Timer(RunningConfig.config.getBossShoot(), () -> {
            synchronized (enemyBullets) {
                bossAircrafts.forEach(bossEnemy -> enemyBullets.addAll(bossEnemy.shoot()));
            }
        }));
        // boss 生成事件
        timerController.add(new Timer(10, () -> {
            RunningConfig.config.getBossScoreThreshold().update(Utils.getTimeMills());
            if (RunningConfig.score > nextBossScore && bossAircrafts.isEmpty()) {
                synchronized (bossAircrafts) {
                    bossAircrafts.add(new BossEnemyFactory(() -> {
                        nextBossScore = RunningConfig.config.getBossScoreThreshold().getScaleNow().getX() + RunningConfig.score;
                        BossEnemyFactory.clearInstance();
                    }).create());
                }
            }
        }));
        // 输出当前 config
        if (RunningConfig.difficulty != Easy) {
            timerController.add(new Timer(2000, () -> RunningConfig.config.printNow()));
        } else {
            Utils.getLogger().info("简单模式 Config 将不会改变: " + RunningConfig.config);
        }
    }

    private void onGameOver() {
        // 游戏结束
        if (future != null) {
            future.cancel(true);
        }
        // 游戏结束
        gameOverFlag = true;
        ResourceProvider.getInstance().startMusic(MusicType.GAME_OVER, true);
        ResourceProvider.getInstance().stopAllMusic();
        Utils.getLogger().info("Game Over!");
        if (onFinish != null) {
            onFinish.run();
        }
    }

    protected Runnable getMainTask() {
        return () -> {
            try {
                timerController.update();
                // execute all
                timerController.execute();
                // 按键处理
                if (onFrame != null) {
                    onFrame.run();
                }
                // 所有物体移动
                synchronized (allFlyingObjects) {
                    allFlyingObjects.forEach(objList -> objList.forEach(AbstractObject::forward));
                }
                // 撞击检测
                crashCheckAction();
                // 后处理
                synchronized (allFlyingObjects) {
                    allFlyingObjects.forEach(objList -> objList.removeIf(AbstractObject::notValid));
                }
                // 每个时刻重绘界面
                if (onPaint != null) {
                    onPaint.run();
                }
                // repaint();
                // 游戏结束检查和处理
                if (heroAircraft.getHp() <= 0 && !Constants.DEBUG_NO_DEATH && !gameOverFlag) {
                    onGameOver();
                }
                timerController.done();
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Utils.getLogger().warn("this thread will exit: " + e);
            }
        };
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        ResourceProvider.getInstance().musicLoadAll();
        Utils.getLogger().info("Game action start with difficulty: " + RunningConfig.difficulty);
        addTimers();
        ResourceProvider.getInstance().startLoopMusic(MusicType.BGM);
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
            }
        }

        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) {
                // 已被其他子弹击毁的敌机，不再检测
                // 避免多个子弹重复击毁同一敌机的判定
                continue;
            }
            // 英雄机 与 敌机 相撞
            if (heroAircraft.crash(enemyAircraft)) {
                enemyAircraft.vanish(true);
                heroAircraft.decreaseHp(RunningConfig.config.getAircraftCrashDecreaseHp());
                heroAircraft.startInvincibleState();
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

    public List<List<? extends AbstractFlyingObject>> getAllFlyingObjects() {
        return allFlyingObjects;
    }

    public double getNextBossScore() {
        return nextBossScore;
    }

    public TimerController getTimerController() {
        return timerController;
    }

    public XLayout getLayout() {
        return layout;
    }
}
