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
import work.chiro.game.x.ui.view.XJoySticks;
import work.chiro.game.x.ui.view.XView;

@SuppressWarnings("FieldCanBeLocal")
public class BattleActivity extends XActivity {
    private XButton buttonBack;
    private LaSignora signora;

    private static class ButtonGroupItem {
        public XButton button;
        public DelayTimer delayTimer;
        public double coolDownDelayMs;
        public XViewCallback onClick;

        public ButtonGroupItem(XButton button, DelayTimer delayTimer, double coolDownDelayMs, XViewCallback onClick) {
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
        // synchronized (XGraphics.class) {
            new LaSignora().preLoadResources(ResourceProvider.getInstance().getXGraphics());
            ResourceProvider.getInstance().stopXGraphics();
        // }
        Utils.getLogger().info("loading resource done");
        signora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight * 1. / 2), new AbstractAction(null));
        signora.getAnimateContainer().setThing(signora);
        getGame().addThing(signora);
        getGame().getObjectController().setTarget(signora);
        XJoySticks joySticks = (XJoySticks) findViewById("joySticks");
        getGame().getObjectController().setJoySticks(joySticks);

        buttonGroup.addAll(List.of(
                new ButtonGroupItem(
                        (XButton) findViewById("buttonSkillAttack"),
                        signora.getSkillAttackDelayTask(),
                        signora.getBasicAttributes().getSkillAttackCoolDown() * 1000,
                        (xView, xEvent) -> {
                            Utils.getLogger().info("skill attack");
                            signora.skillAttack();
                        }
                ),
                new ButtonGroupItem(
                        (XButton) findViewById("buttonChargedAttack"),
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

        getGame().getTimerController().add(new Timer(5000, (controller, timer) -> {
            Utils.getLogger().info("generate enemies");
            ShogunateSoldier shogunateSolder = new ShogunateSoldier(
                    new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight * 1. / 2),
                    new AbstractAction(null));
            shogunateSolder.getAnimateContainer().setThing(shogunateSolder);
            shogunateSolder.setFlipped(true);
            getGame().addThing(shogunateSolder);
        }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        buttonBack.popEvent(XEventType.Click);
        TimeManager.timeResume();
        getGame().getCharacters().clear();
        getGame().getAttacks().clear();
        getGame().getTimerController().clear();
    }

    protected void applyActionToButton(XView button, DelayTimer delayTimer, double maxTimeMs) {
        if (!delayTimer.isValid() && delayTimer.getTimeMark() != 0) {
            double since = (maxTimeMs - (TimeManager.getTimeMills() - delayTimer.getTimeMark())) / 1000;
            String sinceString = String.format(Locale.CHINA, "%.1f", since);
            button.setAlpha(new Scale(0.6));
            button.setText(sinceString);
        } else {
            button.setAlpha(new Scale(1));
            button.setText("");
        }
    }

    @Override
    protected void onFrame() {
        super.onFrame();
        if (signora == null) return;
        buttonGroup.forEach(buttonGroupItem -> applyActionToButton(buttonGroupItem.button, buttonGroupItem.delayTimer, buttonGroupItem.coolDownDelayMs));
    }
}
