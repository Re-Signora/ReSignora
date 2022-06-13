package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;

public class AircraftWarActivity extends XActivity {
    public AircraftWarActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("aircraft");
    }
}
