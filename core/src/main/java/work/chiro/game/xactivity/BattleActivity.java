package work.chiro.game.xactivity;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.shogunate.ShogunateSoldier;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.utils.timer.Timer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.ui.builder.XViewCallback;
import work.chiro.game.x.ui.event.XEventType;
import work.chiro.game.x.ui.view.XButton;
import work.chiro.game.x.ui.view.XDialogue;
import work.chiro.game.x.ui.view.XJoySticks;
import work.chiro.game.x.ui.view.XView;

@SuppressWarnings("FieldCanBeLocal")
public class BattleActivity extends XActivity {
    protected XButton buttonBack;
    protected LaSignora signora;
    protected XDialogue dialogue;

    private static class ButtonGroupItem {
        public XView button;
        public DelayTimer delayTimer;
        public double coolDownDelayMs;
        public XViewCallback onClick;

        public ButtonGroupItem(XView button, DelayTimer delayTimer, double coolDownDelayMs, XViewCallback onClick) {
            this.button = button;
            this.delayTimer = delayTimer;
            this.coolDownDelayMs = coolDownDelayMs;
            this.onClick = onClick;
        }
    }

    private final List<ButtonGroupItem> buttonGroup = new LinkedList<>();

    public BattleActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("battle");
        buttonBack = (XButton) findViewById("button返回");
        buttonBack.setOnClick((xView, xEvent) -> finish());
        Utils.getLogger().info("loading resource...");
        synchronized (ResourceProvider.getInstance().waitXGraphicsObject()) {
            new LaSignora().preLoadResources(ResourceProvider.getInstance().getXGraphics());
            ResourceProvider.getInstance().stopXGraphics();
        }
        Utils.getLogger().info("loading resource done");
        signora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 4, RunningConfig.windowHeight * 2. / 3), new AbstractAction(null));
        signora.getAnimateContainer().setThing(signora);
        getGame().addThing(signora);
        getGame().getObjectController().setTarget(signora);
        XJoySticks joySticks = (XJoySticks) findViewById("joySticks");
        getGame().getObjectController().setJoySticks(joySticks);
        getGame().getObjectController().setBattleActivity(this);

        buttonGroup.addAll(List.of(
                new ButtonGroupItem(
                        findViewById("buttonSkillAttack"),
                        signora.getSkillAttackDelayTask(),
                        signora.getBasicAttributes().getSkillAttackCoolDown() * 1000,
                        (xView, xEvent) -> {
                            Utils.getLogger().info("skill attack");
                            signora.skillAttack();
                        }
                ),
                new ButtonGroupItem(
                        findViewById("buttonChargedAttack"),
                        signora.getChargedAttackDelayTask(),
                        signora.getBasicAttributes().getChargedAttackCoolDown() * 1000,
                        (xView, xEvent) -> {
                            Utils.getLogger().info("charged attack");
                            signora.chargedAttack();
                        }
                )
        ));

        buttonGroup.forEach(buttonGroupItem -> {
            buttonGroupItem.button.setOnClick(buttonGroupItem.onClick);
            buttonGroupItem.button.setFont("genshin");
        });

        dialogue = (XDialogue) findViewById("dialogue");
        dialogue.setVisible(false);

        Runnable createEnemies = () -> {
            Utils.getLogger().info("generate enemies");
            ShogunateSoldier shogunateSolder = new ShogunateSoldier(
                    Utils.randomPosition(
                            new Vec2().fromVector(new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight).times(0.1)),
                            new Vec2().fromVector(new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight).times(0.9))),
                    new AbstractAction(null));
            shogunateSolder.getAnimateContainer().setThing(shogunateSolder);
            shogunateSolder.setFlipped(true);
            getGame().addEnemyThing(shogunateSolder);
        };

        createEnemies.run();

        getGame().getTimerController().add(getClass(), new Timer(2000, (controller, timer) -> createEnemies.run()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        buttonBack.popEvent(XEventType.Click);
        TimeManager.timeResume();
        getGame().getCharacters().clear();
        getGame().getEnemies().clear();
        getGame().getAttacks().clear();
        getGame().getUnderAttacks().clear();
        getGame().getTimerController().clear();
    }

    protected void applyActionToButton(XView button, DelayTimer delayTimer, double maxTimeMs) {
        if (!delayTimer.isValid() && delayTimer.getTimeMark() != 0) {
            double since = (maxTimeMs - (TimeManager.getTimeMills() - delayTimer.getTimeMark())) / 1000;
            String sinceString = String.format(Locale.CHINA, "%.1f", since);
            button.setAlpha(new Scale(0.6));
            button.setText(sinceString);
        } else {
            // Android 不靠 keyUp 恢复透明度
            if (!RunningConfig.modePC) button.setAlpha(new Scale(1));
            button.setText("");
        }
    }

    @Override
    protected void onFrame() {
        super.onFrame();
        if (signora == null) return;
        buttonGroup.forEach(buttonGroupItem -> applyActionToButton(buttonGroupItem.button, buttonGroupItem.delayTimer, buttonGroupItem.coolDownDelayMs));
    }

    public void normalAttack() {
    }

    public void skillAttack() {
        findViewById("buttonSkillAttack").setAlpha(new Scale(1));
        signora.skillAttack();
    }

    public void chargedAttack() {
        findViewById("buttonChargedAttack").setAlpha(new Scale(1));
        signora.chargedAttack();
    }

    public void beforeNormalAttack() {
    }

    public void beforeSkillAttack() {
        findViewById("buttonSkillAttack").setAlpha(new Scale(0.6));
    }

    public void beforeChargedAttack() {
        findViewById("buttonChargedAttack").setAlpha(new Scale(0.6));
    }
}
