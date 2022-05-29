package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.ui.XButton;

public class HomeActivity extends XActivity {
    public HomeActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("main");
        XButton historyButton = (XButton) findViewById("buttonStoryMode");
        historyButton.setOnClick((xView, xEvent) -> startActivity(StageSelectActivity.class));
        // Vec2 pos = HeroAircraftFactory.getInstance().getPosition();
        // Butterfly butterfly = new Butterfly(pos);
        // getGame().getThings().add(butterfly);
    }
}
