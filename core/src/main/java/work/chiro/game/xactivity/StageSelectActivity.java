package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.ui.event.XEventType;
import work.chiro.game.x.ui.view.XButton;

public class StageSelectActivity extends XActivity {
    private XButton buttonBack;

    public StageSelectActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("stageSelect");
        XButton buttonStage01 = (XButton) findViewById("button按钮1");
        buttonStage01.setOnClick((xView, xEvent) -> startActivity(AircraftWarActivity.class));
        XButton buttonStage02 = (XButton) findViewById("button按钮2");
        buttonStage02.setOnClick((xView, xEvent) -> startActivity(BattleActivity.class));
        buttonBack = (XButton) findViewById("button返回");
        buttonBack.setOnClick((xView, xEvent) -> finish());
    }

    @Override
    protected void onStop() {
        super.onStop();
        buttonBack.popEvent(XEventType.Click);
        TimeManager.timeResume();
    }
}
