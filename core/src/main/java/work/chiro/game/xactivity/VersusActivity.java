package work.chiro.game.xactivity;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.dialogue.DialogueBean;
import work.chiro.game.dialogue.DialogueManager;
import work.chiro.game.game.Game;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XBundle;

public class VersusActivity extends BattleActivity {
    protected LaSignora eSignora;
    protected boolean connected = true;

    public VersusActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eSignora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 4, RunningConfig.windowHeight * 2. / 3), new AbstractAction(null));
        eSignora.getAnimateContainer().setThing(eSignora);
        eSignora.getDynamicCharacterAttributes().setEnemy(true);
        eSignora.normalAttack();
        getGame().getObjectController().setSecondaryTarget(eSignora);
        getGame().addThing(eSignora);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!connected) {
            TimeManager.timePause();
            Utils.getLogger().info("pause");
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
                TimeManager.timeResume();
                connected = true;
                dialogue.setDialogueManager(new DialogueManager() {
                    @Override
                    public DialogueBean getDialogue() {
                        return new DialogueBean("连接成功");
                    }
                });
                dialogue.setOnNextText((xView, xEvent) -> dialogue.setVisible(false));
            }).start();
        }
    }

    @Override
    protected void onFrame() {
        super.onFrame();
        if (eSignora != null) {
            eSignora.setPosition(new Vec2(RunningConfig.windowWidth - signora.getLocationX(), signora.getLocationY()));
            eSignora.setFlipped(!signora.isFlipped());
        }
    }
}
