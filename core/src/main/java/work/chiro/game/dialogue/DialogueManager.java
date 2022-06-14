package work.chiro.game.dialogue;

/**
 * 对话管理器，用于从文件中动态加载对话等。<br/>
 * TODO: 从文件中动态加载对话
 */
public abstract class DialogueManager implements DialogueGetter {
    @Override
    public DialogueBean getDialogue() {
        return new DialogueBean();
    }
}
