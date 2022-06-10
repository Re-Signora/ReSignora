package work.chiro.game.xactivity;

import java.util.Locale;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.ui.event.XEventType;
import work.chiro.game.x.ui.view.XButton;
import work.chiro.game.x.ui.view.XJoySticks;

public class BattleActivity extends XActivity {
    private XButton buttonBack;
    private LaSignora signora;
    private XButton buttonSkillAttack;
    private XButton buttonChargedAttack;

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
        synchronized (XGraphics.class) {
            new LaSignora().preLoadResources(ResourceProvider.getInstance().getXGraphics());
            ResourceProvider.getInstance().stopXGraphics();
        }
        Utils.getLogger().info("loading resource done");
        signora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight * 1. / 2), new AbstractAction(null));
        signora.getAnimateContainer().setThing(signora);
        getGame().addThing(signora);
        getGame().getObjectController().setTarget(signora);
        XJoySticks joySticks = (XJoySticks) findViewById("joySticks");
        getGame().getObjectController().setJoySticks(joySticks);

        buttonSkillAttack = (XButton) findViewById("buttonSkillAttack");
        buttonChargedAttack = (XButton) findViewById("buttonChargedAttack");
        buttonSkillAttack.setOnClick((xView, xEvent) -> {
            Utils.getLogger().info("skill attack");
            signora.skillAttack();
        });
        buttonSkillAttack.setFont("genshin");
        buttonChargedAttack.setOnClick((xView, xEvent) -> {
            Utils.getLogger().info("charged attack");
            signora.chargedAttack();
        });
        buttonChargedAttack.setFont("genshin");
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

    @Override
    protected void onFrame() {
        super.onFrame();
        if (signora == null) return;
        if (!signora.getSkillAttackDelayTask().isValid() && signora.getSkillAttackDelayTask().getTimeMark() != 0) {
            double since = signora.getBasicAttributes().getSkillAttackCoolDown() -
                    ((TimeManager.getTimeMills() - signora.getSkillAttackDelayTask().getTimeMark())) / 1000;
            String sinceString = String.format(Locale.CHINA, "%.1f", since);
            buttonSkillAttack.setAlpha(new Scale(0.6));
            buttonSkillAttack.setText(sinceString);
        } else {
            buttonSkillAttack.setAlpha(new Scale(1));
            buttonSkillAttack.setText("");
        }
    }
}
