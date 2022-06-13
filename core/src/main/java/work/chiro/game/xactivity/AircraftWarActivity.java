package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.ui.view.XJoySticks;

public class AircraftWarActivity extends BattleActivity {
    public AircraftWarActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("aircraft");
        XJoySticks joySticks = (XJoySticks) findViewById("joySticks");
        getGame().getObjectController().setJoySticks(joySticks);
        getGame().getObjectController().setBattleActivity(this);
    }
}
