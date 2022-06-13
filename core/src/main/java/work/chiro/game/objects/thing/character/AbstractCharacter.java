package work.chiro.game.objects.thing.character;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.Constants;
import work.chiro.game.logic.DamageCalculator;
import work.chiro.game.logic.attributes.dynamic.DynamicCharacterAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.objects.thing.attack.AbstractAttack;
import work.chiro.game.objects.thing.attack.UnderAttack;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.colors.DrawColor;

public abstract class AbstractCharacter extends AbstractThing<BasicCharacterAttributes, AbstractAction>
        implements UnderAttack {
    protected final DelayTimer skillAttackDelayTask = getDelayTimer();
    protected final DelayTimer chargedAttackDelayTask = getDelayTimer();
    protected final DynamicCharacterAttributes dynamicCharacterAttributes;

    protected abstract DelayTimer getDelayTimer();

    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, attributesClass, posInit, animateContainer, sizeInit, rotationInit, alpha);
        dynamicCharacterAttributes = DynamicCharacterAttributes.initFromCharacterAttributes(getBasicAttributes());
        skillAttackDelayTask.setValid();
        chargedAttackDelayTask.setValid();
        if (getBasicAttributes().isSizeAvailable()) {
            Utils.getLogger().warn("setSize: {}", getBasicAttributes().getSize());
            setSize(getBasicAttributes().getSize());
        }
    }

    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer) {
        this(labelName, attributesClass, posInit, animateContainer, null, null, null);
    }

    public AbstractCharacter() {
        super();
        dynamicCharacterAttributes = null;
    }

    public void normalAttack() {
    }

    public void skillAttack() {
    }

    public void chargedAttack() {
    }

    public void beforeNormalAttack() {
    }

    public void beforeSkillAttack() {
    }

    public void beforeChargedAttack() {
    }

    public DynamicCharacterAttributes getDynamicCharacterAttributes() {
        return dynamicCharacterAttributes;
    }

    protected void bearDamage(int damage) {
        Utils.getLogger().warn("bearDamage: {}", damage);
        if (getHp() >= damage) {
            getDynamicCharacterAttributes().setHp(getHp() - damage);
        } else {
            getDynamicCharacterAttributes().setHp(0);
            vanish();
        }
    }

    @Override
    public void applyAttack(AbstractAttack attack) {
        bearDamage(DamageCalculator.calculate(attack, this));
        attack.vanish();
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

    @Override
    public boolean isCrashAttack(AbstractAttack attack) {
        return crash(attack);
    }

    public int getHp() {
        return getDynamicCharacterAttributes().getHp();
    }

    @SuppressWarnings("SameParameterValue")
    protected void drawHp(XGraphics g, int colorFront, int colorBack, boolean forceDraw) {
        if (getHp() == getBasicAttributes().getMaxHp() && !forceDraw) {
            return;
        }
        int hpBarHeight = Constants.DRAW_HP_BAR;
        g.setColor(colorBack)
                .fillRect(getLocationX() - getWidth() / 2, getLocationY() - getHeight() / 2,
                        getWidth(), hpBarHeight)
                .setColor(colorFront)
                .fillRect(getLocationX() - getWidth() / 2, getLocationY() - getHeight() / 2,
                        getWidth() / getBasicAttributes().getMaxHp() * getHp(), hpBarHeight);
    }

    protected void drawHp(XGraphics g) {
        drawHp(g, DrawColor.red, DrawColor.gray, true);
    }

    @Override
    @Deprecated
    public void draw(XGraphics g, boolean center) {
        super.draw(g, center);
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
        drawHp(g);
    }
}
