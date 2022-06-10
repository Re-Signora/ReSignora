package work.chiro.game.xactivity;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
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
        LaSignora signora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight * 1. / 2), new AbstractAction(null));
        signora.getAnimateContainer().setThing(signora);
        getGame().addThing(signora);
        getGame().getObjectController().setTarget(signora);
        XJoySticks joySticks = (XJoySticks) findViewById("joySticks");
        getGame().getObjectController().setJoySticks(joySticks);

        XButton buttonSkillAttack = (XButton) findViewById("buttonSkillAttack");
        XButton buttonChargedAttack = (XButton) findViewById("buttonChargedAttack");
        buttonSkillAttack.setOnClick((xView, xEvent) -> Utils.getLogger().info("skill attack"));
        buttonChargedAttack.setOnClick((xView, xEvent) -> Utils.getLogger().info("charged attack"));
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
}
