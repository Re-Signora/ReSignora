package work.chiro.game.xactivity;

import work.chiro.game.game.Game;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.activity.XBundle;
import work.chiro.game.x.ui.event.XEventType;
import work.chiro.game.x.ui.view.XButton;

public class HomeActivity extends XActivity {
    private XButton buttonBack;
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
        buttonBack = (XButton) findViewById("button返回");
        buttonBack.setOnClick((xView, xEvent) -> finish());

        // AtomicReference<String> ha = new AtomicReference<>("哈");
        // XDialogue dialogue = (XDialogue) findViewById("dialogueTest");
        // dialogue.setDialogueManager(new DialogueManager() {
        //     @Override
        //     public DialogueBean getDialogue() {
        //         return new DialogueBean("旅行者", ha.get());
        //     }
        // });
        // dialogue.setOnNextText((xView, xEvent) -> {
        //     Utils.getLogger().info("next text! {}, {}", xView, xEvent);
        //     ha.set(ha + "哈");
        // });

        // getLayout().addView(new DamagePopup(new Vec2(RunningConfig.windowWidth * 1. / 2, RunningConfig.windowHeight * 1. / 2), Element.Cryo, 1100));
    }

    @Override
    protected void onStop() {
        super.onStop();
        TimeManager.timeResume();
        buttonBack.popEvent(XEventType.Click);
    }
}
