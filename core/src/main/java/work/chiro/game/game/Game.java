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

/**
 * 游戏主体类
 */
public class Game {
    /**
     * Singleton 模式保存 Game 实例，方便全局获取
     */
    private static Game instance = null;

    /**
     * 获取 Game 实例，必须保证已经显式创建
     *
     * @return Game instance
     */
    public static Game getInstance() {
        assert instance != null;
        return instance;
    }

    /**
     * 显式创建 Game 实例
     *
     * @param characterController 角色控制器
     * @return Game
     */
    public static Game createInstance(CharacterController characterController) {
        Utils.getLogger().info("will create Game!");
        assert instance == null;
        instance = new Game(characterController);
        return instance;
    }

    /**
     * 清除创建的 Game 实例，重启游戏时用到
     *
     * @return 旧的 Game 实例
     */
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

    /**
     * 背景列表
     */
    private final List<AbstractBackground> backgrounds = new LinkedList<>();
    /**
     * 角色列表
     */
    private final List<AbstractCharacter> characters = new LinkedList<>();
    /**
     * 敌方角色列表
     */
    private final List<AbstractCharacter> enemies = new LinkedList<>();
    /**
     * 己方攻击列表
     */
    private final List<AbstractAttack> attacks = new LinkedList<>();
    /**
     * 敌方攻击列表
     */
    private final List<AbstractAttack> enemiesAttacks = new LinkedList<>();
    /**
     * 己方受攻击对象列表
     */
    private final List<UnderAttack> underAttacks = new LinkedList<>();
    /**
     * 敌方受攻击对象列表
     */
    private final List<UnderAttack> enemiesUnderAttacks = new LinkedList<>();
    /**
     * 所有的 things
     */
    private final List<List<? extends AbstractThing<?, ?>>> allThings = Arrays.asList(
            characters, enemies, attacks, enemiesAttacks
    );
    /**
     * 所有的受攻击对象
     */
    private final List<List<? extends UnderAttack>> allUnderAttacks = Arrays.asList(
            underAttacks, enemiesUnderAttacks
    );
    private boolean gameOverFlag = false;
    /**
     * 用于取消线程池运行
     */
    private Future<?> future = null;
    /**
     * 计时器控制器
     */
    private final TimerController timerController = new TimerController();
    /**
     * 游戏结束时候的钩子
     */
    private BasicCallback onFinish = null;
    /**
     * 每次重绘画面钩子
     */
    private BasicCallback onPaint = null;
   /**
     * 每帧钩子
     */
    private BasicCallback onFrame = null;
//    是在哪儿把这个变成非null了啊？？？？没找到qwq
    /**
     * 角色控制器
     */
    private final CharacterController characterController;
    /**
     * 活动管理器
     */
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

    /**
     * 游戏退出钩子
     */
    private BasicCallback onExit = null;

    public void setOnExit(BasicCallback onExit) {
        this.onExit = onExit;
    }

    /**
     * 清除所有 things
     */
    @Deprecated
    public void clearThings() {
        characters.clear();
        attacks.clear();
    }

    /**
     * 重置游戏状态
     */
    @Deprecated
    private void resetStates() {
        gameOverFlag = false;
        RunningConfig.score = 0;
        RunningConfig.config = new ConfigFactory(RunningConfig.difficulty).create();
        clearThings();
        timerController.clear();
    }

    private Game(CharacterController characterController) {
        this.characterController = characterController;
//        RunningConfig.config = new ConfigFactory(RunningConfig.difficulty).create();
        backgrounds.add(null);
        backgrounds.add(null);
        activityManager.setOnSwitchActivity(newActivity -> {
            Utils.getLogger().info("setOnSwitchActivity({})", newActivity);
            if (newActivity == null) return;
            if (newActivity.getLayout() == null) return;
            if (getTopLayout().getBackground() != null) {
//                if (backgrounds.get(1) != null) {
//                    backgrounds.set(0, backgrounds.get(1));
//                }

//                AbstractBackground abstractBackground = new BasicBackgroundFactory(getTopLayout().getBackground()).create();
//                backgrounds.add(abstractBackground);
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

    @Deprecated
    private void characterAttack() {
        synchronized (attacks) {
            Utils.getLogger().debug("characters attacks");
        }
    }
//    钩子判断是否完成工作？
    public void setOnFinish(BasicCallback onFinish) {
        this.onFinish = onFinish;
    }

    public void setOnPaint(BasicCallback onPaint) {
        this.onPaint = onPaint;
    }

    public void setOnFrame(BasicCallback onFrame) {
        this.onFrame = onFrame;
    }

    /**
     * 添加一下默认的定时器？？？
     */
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

    /**
     * 当游戏退出，game 实例销毁之前
     */
    private void onGameExit() {
//        这是干什么的啊？？？？
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

    @Deprecated
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

    /**
     * 获取主线程循环逻辑
     *
     * @return 主线程循环逻辑
     */
    protected Runnable getMainTask() {
//        前面这个return是啥？？？？
        return () -> {
//            好烦哦，各个模块都找不到所对应的地方，好烦啊┭┮﹏┭┮？？？？
//            对了，除此以外，创造敌人，和创造魔偶的地方在哪里啊
            try {
//                我觉得这个是动画处理，但是我没有证据，我看不懂qwq？？？？
                timerController.update();
//                這個應該是主要的調用了qwq
                // execute all
                timerController.execute();
                // 按键处理
                if (onFrame != null) {
                    onFrame.run();
                }
                getActivityManager().onFrame();
                // 所有物体移动
//                你这个跳太多层了qwq，找不到getActivityManager是干嘛的┭┮﹏┭┮？？？？
                synchronized (allThings) {
                    allThings.forEach(objList -> objList.forEach(AbstractObject::forward));
                }
//                循环把每一个往前移？
                getTopLayout().forEach(AbstractObject::forward);
                // 撞击检测
                crashCheckAction();
                // 后处理
//                没细看，但想必应该就是没血了就消失，然后不显示qwq吧？？？？
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
//                又是找不到run了个啥的一天，呜呜呜，你是在学可莉炸鱼嘛？？？？
                // 游戏结束检查和处理
                // if (!Constants.DEBUG_NO_DEATH && !gameOverFlag) {
                //     onGameOver();
                // }
                timerController.done();
                Thread.sleep(1);
            } catch (InterruptedException e) {
//                我还是没看懂线程是从哪里来的>w<啊啊啊啊？？？？
                Utils.getLogger().warn("this thread will exit: " + e);
            }
        };
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        ResourceProvider.getInstance().musicLoadAll();
//        加载音乐？？？？
        Utils.getLogger().info("Game action start with difficulty: " + RunningConfig.difficulty);
//        我觉得，这个东西，应该是不需要的吧qwq
        setTimers();
        ResourceProvider.getInstance().startLoopMusic(MusicType.BGM);
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable mainTask = getMainTask();
        int timeInterval = 1;
        // 以固定延迟时间进行执行本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
//        这又是个什么函数，没有看太懂.....
        future = executorService.scheduleWithFixedDelay(mainTask, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * 对一对 attacks 和 underAttacks 检查并处理碰撞事件
     *
     * @param attackList      attacks
     * @param underAttackList underAttacks
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private void crashCheckAttacksAndUnderAttacks(List<AbstractAttack> attackList, List<UnderAttack> underAttackList) {
        synchronized (attackList) {
            attackList.forEach(abstractAttack -> {
                synchronized (underAttackList) {
                    underAttackList.forEach(underAttack -> {
                        if (underAttack.isCrashAttack(abstractAttack)) {
//                            ？？？？我没找到撞击的判断程序在哪里
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

    /**
     * 对所有对象按照 Y 坐标从大到小排序，用于按顺序绘制
     *
     * @return 排序后的 List
     */
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
        if (thing.getBasicDynamicAttributes().isEnemy()) {
            return addEnemyThing(thing);
        }
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

    /**
     * 添加一个敌方 Thing
     *
     * @param thing 敌方 Thing
     * @return this
     */
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

    /**
     * 移除 inValid == false 的 XView
     */
    public void removeInvalidViews() {
        synchronized (Game.class) {
            getTopLayout().removeIf(view -> !view.isValid());
        }
    }
}
