package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.x.activity.XBundle;

public class VersusActivity extends BattleActivity {
    protected LaSignora eSignora;
    public VersusActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
