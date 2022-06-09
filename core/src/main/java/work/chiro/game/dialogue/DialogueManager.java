package work.chiro.game.dialogue;

public class DialogueManager implements DialogueGetter {
    @Override
    public DialogueBean getDialogue() {
        return new DialogueBean();
    }
}
