package work.chiro.game.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import work.chiro.game.config.ConfigFactory;
import work.chiro.game.config.Constants;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.objects.AbstractObject;
import work.chiro.game.objects.background.AbstractBackground;
import work.chiro.game.objects.background.BasicBackgroundFactory;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.objects.thing.attack.AbstractAttack;
import work.chiro.game.objects.thing.attack.UnderAttack;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.resource.MusicType;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.callback.BasicCallback;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.utils.timer.TimerController;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XActivityManager;
import work.chiro.game.x.compatible.CharacterController;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.ui.layout.XLayout;
import work.chiro.game.x.ui.layout.XLayoutManager;
import work.chiro.game.xactivity.HomeActivity;

public class Game {
    private static Game instance = null;

    public static Game getInstance() {
        assert instance != null;
        return instance;
    }

    public static Game createInstance(CharacterController characterController) {
        Utils.getLogger().info("will create Game!");
        assert instance == null;
        instance = new Game(characterController);
        return instance;
    }

    public static Game clearInstance() {
        Game lastGame = instance;
        instance = null;
        return lastGame;
    }

    /**
     * 线程池，自动管理
     */
    @SuppressWarnings("AlibabaThreadPoolCreation")
    static private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(Constants.GAME_POOL_SIZE, MyThreadFactory.getInstance());

    private final List<AbstractBackground> backgrounds = new LinkedList<>();
    private final List<AbstractCharacter> characters = new LinkedList<>();
    private final List<AbstractCharacter> enemies = new LinkedList<>();
    private final List<AbstractAttack> attacks = new LinkedList<>();
    private final List<AbstractAttack> enemiesAttacks = new LinkedList<>();
    private final List<UnderAttack> underAttacks = new LinkedList<>();
    private final List<UnderAttack> enemiesUnderAttacks = new LinkedList<>();
    private final List<List<? extends AbstractThing<?, ?>>> allThings = Arrays.asList(
            characters, enemies, attacks, enemiesAttacks
    );
    private final List<List<? extends UnderAttack>> allUnderAttacks = Arrays.asList(
            underAttacks, enemiesUnderAttacks
    );
    private boolean gameOverFlag = false;
    private Future<?> future = null;
    private final TimerController timerController = new TimerController();
    private BasicCallback onFinish = null;
    private BasicCallback onPaint = null;
    private BasicCallback onFrame = null;
    private final CharacterController characterController;
    private final XActivityManager activityManager = new XActivityManager(this);

    public XActivityManager getActivityManager() {
        return activityManager;
    }

    public XLayoutManager getLayoutManager() {
        return activityManager.getLayoutManager();
    }

    public XLayout getTopLayout() {
        return activityManager.getTopLayout();
    }

    public XActivity getTopActivity() {
        return getActivityManager().getTop();
    }

    private BasicCallback onExit = null;

    public void setOnExit(BasicCallback onExit) {
        this.onExit = onExit;
    }

    public void clearThings() {
        characters.clear();
        attacks.clear();
    }

    // public void resetStates() {
    private void resetStates() {
        gameOverFlag = false;
        RunningConfig.score = 0;
        RunningConfig.config = new ConfigFactory(RunningConfig.difficulty).create();
        clearThings();
        timerController.clear();
    }

    private Game(CharacterController characterController) {
        this.characterController = characterController;
        RunningConfig.config = new ConfigFactory(RunningConfig.difficulty).create();
        backgrounds.add(null);
        backgrounds.add(null);
        activityManager.setOnSwitchActivity(newActivity -> {
            Utils.getLogger().info("setOnSwitchActivity({})", newActivity);
            if (newActivity == null) return;
            if (newActivity.getLayout() == null) return;
            if (getTopLayout().getBackground() != null) {
                if (backgrounds.get(1) != null) {
                    backgrounds.set(0, backgrounds.get(1));
                }
                backgrounds.set(1, new BasicBackgroundFactory(getTopLayout().getBackground()).create());
            }
        });
        activityManager.setOnFinishAllActivities(this::onGameExit);
        activityManager.startActivityWithBundle(HomeActivity.class);
        Utils.getLogger().info("Game instance created!");
    }

    public List<AbstractBackground> getBackgrounds() {
        return backgrounds;
    }

    private void characterAttack() {
        synchronized (attacks) {
            Utils.getLogger().debug("characters attacks");
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

    public void setTimers() {
        timerController.init(TimeManager.getTimeMills());
        // // 英雄射击事件
        // timerController.add(new Timer(RunningConfig.config.getHeroShoot(), (controller, timer) -> {
        //     if (RunningConfig.autoShoot) {
        //         characterAttack();
        //     } else {
        //         if (characterController.isShootPressed()) {
        //             characterAttack();
        //         }
        //     }
        // }));
    }

    private void onGameExit() {
        if (future != null) {
            future.cancel(true);
        }
        if (onExit != null) {
            getTimerController().disable();
            clearInstance();
            onExit.run();
        }
        if (onFinish != null) {
            onFinish.run();
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
                getActivityManager().onFrame();
                // 所有物体移动
                synchronized (allThings) {
                    allThings.forEach(objList -> objList.forEach(AbstractObject::forward));
                }
                getTopLayout().forEach(AbstractObject::forward);
                // 撞击检测
                crashCheckAction();
                // 后处理
                synchronized (allThings) {
                    allThings.forEach(objList -> objList.removeIf(obj -> !obj.isValid()));
                }
                synchronized (allUnderAttacks) {
                    allUnderAttacks.forEach(underAttacksList -> underAttacksList.removeIf(underAttack -> !underAttack.getRelativeCharacter().isValid()));
                }
                // 每个时刻重绘界面
                if (onPaint != null) {
                    onPaint.run();
                }
                // 游戏结束检查和处理
                // if (!Constants.DEBUG_NO_DEATH && !gameOverFlag) {
                //     onGameOver();
                // }
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
        setTimers();
        ResourceProvider.getInstance().startLoopMusic(MusicType.BGM);
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable mainTask = getMainTask();
        int timeInterval = 1;
        // 以固定延迟时间进行执行本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
        future = executorService.scheduleWithFixedDelay(mainTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private void crashCheckAttacksAndUnderAttacks(List<AbstractAttack> attackList, List<UnderAttack> underAttackList) {
        synchronized (attackList) {
            attackList.forEach(abstractAttack -> {
                synchronized (underAttackList) {
                    underAttackList.forEach(underAttack -> {
                        if (underAttack.isCrashAttack(abstractAttack)) {
                            underAttack.applyAttack(abstractAttack);
                        }
                    });
                }
            });
        }
    }

    /**
     * 碰撞检测
     */
    private void crashCheckAction() {
        // 检测 Attacks 和 UnderAttacks
        crashCheckAttacksAndUnderAttacks(attacks, enemiesUnderAttacks);
        crashCheckAttacksAndUnderAttacks(enemiesAttacks, underAttacks);
    }

    public List<List<? extends AbstractThing<?, ?>>> getAllThings() {
        return allThings;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public List<AbstractFlyingObject<?>> getSortedThings() {
        List<List<? extends AbstractThing<?, ?>>> allThings = getAllThings();
        List<AbstractFlyingObject<?>> sortedThings = new CopyOnWriteArrayList<>();
        allThings.forEach(things -> {
            synchronized (things) {
                sortedThings.addAll(things);
            }
        });
        Utils.getLogger().debug("before sort: {}", sortedThings);
        sortedThings.sort((a, b) -> {
            double i = a.getAnchor().getY();
            double j = b.getAnchor().getY();
            return Double.compare(i, j);
        });
        Utils.getLogger().debug("after sort: {}", sortedThings);
        return sortedThings;
    }

    public TimerController getTimerController() {
        return timerController;
    }

    public List<AbstractCharacter> getCharacters() {
        return characters;
    }

    public List<AbstractAttack> getAttacks() {
        return attacks;
    }

    public List<UnderAttack> getUnderAttacks() {
        return underAttacks;
    }

    public List<UnderAttack> getEnemiesUnderAttacks() {
        return enemiesUnderAttacks;
    }

    public List<AbstractAttack> getEnemiesAttacks() {
        return enemiesAttacks;
    }

    public List<AbstractCharacter> getEnemies() {
        return enemies;
    }

    public void addOneAttack(AbstractAttack attack) {
        synchronized (attacks) {
            attacks.add(attack);
        }
    }

    public void addOneUnderAttack(UnderAttack underAttack) {
        synchronized (underAttacks) {
            underAttacks.add(underAttack);
        }
    }

    public void addOneEnemiesAttack(AbstractAttack attack) {
        synchronized (enemiesAttacks) {
            enemiesAttacks.add(attack);
        }
    }

    public void addOneEnemiesUnderAttack(UnderAttack underAttack) {
        synchronized (enemiesUnderAttacks) {
            enemiesUnderAttacks.add(underAttack);
        }
    }

    public void addOneCharacter(AbstractCharacter character) {
        synchronized (characters) {
            characters.add(character);
        }
    }

    public void addOneEnemy(AbstractCharacter enemy) {
        synchronized (enemies) {
            enemies.add(enemy);
        }
    }

    public CharacterController getObjectController() {
        return characterController;
    }

    /**
     * 根据类型自动添加对象到某 list 中
     *
     * @param thing thing
     * @return this
     */
    public Game addThing(AbstractThing<?, ?> thing) {
        // 模板匹配不支持
        if (thing instanceof AbstractCharacter) {
            addOneCharacter((AbstractCharacter) thing);
        }
        if (thing instanceof AbstractAttack) {
            addOneAttack((AbstractAttack) thing);
        }
        if (thing instanceof UnderAttack) {
            addOneUnderAttack((UnderAttack) thing);
        }
        return this;
    }

    public Game addEnemyThing(AbstractThing<?, ?> thing) {
        // 模板匹配不支持
        if (thing instanceof AbstractCharacter) {
            addOneEnemy((AbstractCharacter) thing);
        }
        if (thing instanceof AbstractAttack) {
            addOneEnemiesAttack((AbstractAttack) thing);
        }
        if (thing instanceof UnderAttack) {
            addOneEnemiesUnderAttack((UnderAttack) thing);
        }
        return this;
    }

    public void removeInvalidViews() {
        synchronized (Game.class) {
            getTopLayout().removeIf(view -> !view.isValid());
        }
    }
}
