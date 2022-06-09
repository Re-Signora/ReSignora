package work.chiro.game.xactivity;

import work.chiro.game.dialogue.DialogueBean;
import work.chiro.game.dialogue.DialogueManager;
import work.chiro.game.game.Game;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.ui.view.XButton;
import work.chiro.game.x.ui.view.XDialogue;

public class HomeActivity extends XActivity {
    public HomeActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView("main");
        XButton historyButton = (XButton) findViewById("button剧情模式");
        historyButton.setOnClick((xView, xEvent) -> startActivity(StageSelectActivity.class));
        XButton buttonSettings = (XButton) findViewById("button设置");
        buttonSettings.setOnClick((xView, xEvent) -> TimeManager.timePauseToggle());

        XDialogue dialogue = (XDialogue) findViewById("dialogueTest");
        dialogue.setDialogueManager(new DialogueManager() {
            @Override
            public DialogueBean getDialogue() {
                return new DialogueBean("旅行者", "哈哈哈哈哈哈哈哈哈哈哈哈哈");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        TimeManager.timeResume();
    }
}
