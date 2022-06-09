package work.chiro.game.x.ui.view;

import java.util.LinkedList;
import java.util.List;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.dialogue.DialogueManager;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.compatible.colors.UIColors;
import work.chiro.game.x.ui.builder.XViewCallback;

public class XDialogue extends XView {
    private XViewCallback onNextText = null;
    private final List<AbstractThing<?, ?>> displayingThings = new LinkedList<>();
    private DialogueManager dialogueManager;
    private final double dialogueBoxHeightScale = 0.3;

    public XDialogue(DialogueManager dialogueManager) {
        super(new Vec2());
        setSize(new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight));
        this.dialogueManager = dialogueManager;
        setOnClick((xView, xEvent) -> {
            if (onNextText != null) {
                onNextText.onEvent(xView, xEvent);
            }
        });
    }

    public XDialogue() {
        this(null);
    }

    public void setOnNextText(XViewCallback onNextText) {
        this.onNextText = onNextText;
    }

    public void addThing(AbstractThing<?, ?> thing) {
        synchronized (XDialogue.class) {
            displayingThings.add(thing);
        }
    }

    public boolean removeThing(AbstractThing<?, ?> thing) {
        synchronized (XDialogue.class) {
            return displayingThings.remove(thing);
        }
    }

    protected void drawDialogue(XGraphics g) {
        if (dialogueManager == null) return;
        g.setFont(ResourceProvider.getInstance().getFont("genshin", g.getFontSize()));
        double titleScale = 0.2;
        g.applyCoupleColor(DrawColor.getEnumColors(UIColors.Title));
        g.drawBoarderString(
                new Vec2(0, RunningConfig.windowHeight * (1 - dialogueBoxHeightScale)),
                new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight * (dialogueBoxHeightScale * titleScale)),
                dialogueManager.getDialogue().getSpeaker()
        );
        g.applyCoupleColor(DrawColor.getEnumColors(UIColors.Default));
        g.drawBoarderString(
                new Vec2(0, RunningConfig.windowHeight * (1 - (dialogueBoxHeightScale * (1 - titleScale)))),
                new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight * (dialogueBoxHeightScale * titleScale)),
                dialogueManager.getDialogue().getText()
        );
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
        displayingThings.forEach(abstractThing -> g.drawImage(abstractThing.getImage(), 0, 0));
        drawDialogue(g);
    }

    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public void setDialogueManager(DialogueManager dialogueManager) {
        this.dialogueManager = dialogueManager;
    }
}
