package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.objects.aircraft.HeroAircraftFactory;
import work.chiro.game.objects.thing.attack.Butterfly;
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
        getGame().getThings().add(new Butterfly(HeroAircraftFactory.getInstance().getPosition()));
    }
}
