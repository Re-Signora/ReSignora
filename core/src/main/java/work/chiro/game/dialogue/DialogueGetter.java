package work.chiro.game.dialogue;

/**
 * 可以提供 Dialogue 数据的 Feature
 */
public interface DialogueGetter {
    /**
     * 提供 DialogueBean 数据
     * @return DialogueBean
     */
    DialogueBean getDialogue();
}
