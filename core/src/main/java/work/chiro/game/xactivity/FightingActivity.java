package work.chiro.game.xactivity;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;

public class FightingActivity extends XActivity {
    public FightingActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("fighting");
        LaSignora signora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight * 1. / 2), new AbstractAction(null));
        signora.getAnimateContainer().setThing(signora);
        getGame().getCharacters().add(signora);
    }
}
