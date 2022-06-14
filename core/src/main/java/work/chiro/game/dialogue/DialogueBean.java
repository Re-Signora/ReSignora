package work.chiro.game.dialogue;

/**
 * 对话展示数据，每一屏幕对话对应一个 DialogueBean 数据
 */
public class DialogueBean {
    private String speaker = null;
    private String text = "文本";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public DialogueBean(String speaker, String text) {
        this.speaker = speaker;
        this.text = text;
    }

    public DialogueBean(String text) {
        this.text = text;
    }

    public DialogueBean() {
    }
}
