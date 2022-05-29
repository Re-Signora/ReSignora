package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.ui.XButton;

public class StageSelectActivity extends XActivity {
    public StageSelectActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("stageSelect");
        XButton buttonStage02 = (XButton) findViewById("buttonStage02");
        buttonStage02.setOnClick((xView, xEvent) -> finish());
    }
}
