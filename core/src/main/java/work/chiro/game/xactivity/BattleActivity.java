package work.chiro.game.xactivity;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.ui.view.XJoySticks;

public class BattleActivity extends XActivity {

    public BattleActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("battle");
        LaSignora signora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight * 1. / 2), new AbstractAction(null));
        signora.getAnimateContainer().setThing(signora);
        Utils.getLogger().info("loading resource...");
        signora.preLoadResources();
        Utils.getLogger().info("loading resource done");
        getGame().getCharacters().add(signora);
        getGame().getObjectController().setTarget(signora);
        XJoySticks joySticks = (XJoySticks) findViewById("joySticks");
        getGame().getObjectController().setJoySticks(joySticks);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getGame().getCharacters().clear();
        getGame().getAttacks().clear();
        getGame().getTimerController().clear();
    }
}
