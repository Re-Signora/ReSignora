package work.chiro.game.xactivity;

import work.chiro.game.dialogue.DialogueBean;
import work.chiro.game.dialogue.DialogueManager;
import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.x.activity.XBundle;

public class VersusActivity extends BattleActivity {
    protected LaSignora eSignora;
    protected boolean connected = false;
    public VersusActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!connected) {
            TimeManager.timePause();
            MyThreadFactory.getInstance().newThread(() -> {
                dialogue.setVisible(true);
                dialogue.setDialogueManager(new DialogueManager() {
                    @Override
                    public DialogueBean getDialogue() {
                        return new DialogueBean("等待连接……");
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                connected = true;
                dialogue.setDialogueManager(new DialogueManager() {
                    @Override
                    public DialogueBean getDialogue() {
                        return new DialogueBean("连接成功");
                    }
                });
                dialogue.setOnNextText((xView, xEvent) -> dialogue.setVisible(false));
            });
        }
    }
}
