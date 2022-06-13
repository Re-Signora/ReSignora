package work.chiro.game.objects.thing.character;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.logic.attributes.BasicCharacterAttributes;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.objects.thing.attack.AbstractAttack;
import work.chiro.game.objects.thing.attack.UnderAttack;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;

public abstract class AbstractCharacter extends AbstractThing<BasicCharacterAttributes, AbstractAction>
        implements UnderAttack {
    protected final DelayTimer skillAttackDelayTask = getDelayTimer();
    protected final DelayTimer chargedAttackDelayTask = getDelayTimer();

    protected abstract DelayTimer getDelayTimer();

    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, attributesClass, posInit, animateContainer, sizeInit, rotationInit, alpha);
        skillAttackDelayTask.setValid();
        chargedAttackDelayTask.setValid();
        if (getBasicAttributes().sizeAvailable()) {
            Utils.getLogger().warn("setSize: {}", getBasicAttributes().getSize());
            setSize(getBasicAttributes().getSize());
        }
    }

    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer) {
        this(labelName, attributesClass, posInit, animateContainer, null, null, null);
    }

    public AbstractCharacter() {
        super();
    }

    public void normalAttack() {
    }

    public void skillAttack() {
    }

    public void chargedAttack() {
    }

    @Override
    public void applyAttack(AbstractAttack attack) {
        attack.apply(this);
    }

    @Override
    public BasicCharacterAttributes getBasicAttributes() {
        return (BasicCharacterAttributes) super.getBasicAttributes();
    }

    public DelayTimer getSkillAttackDelayTask() {
        return skillAttackDelayTask;
    }

    public DelayTimer getChargedAttackDelayTask() {
        return chargedAttackDelayTask;
    }
}
