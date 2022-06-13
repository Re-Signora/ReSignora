package work.chiro.game.dialogue;

public abstract class DialogueManager implements DialogueGetter {
    @Override
    public DialogueBean getDialogue() {
        return new DialogueBean();
    }
}
