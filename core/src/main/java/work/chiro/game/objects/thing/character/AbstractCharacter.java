package work.chiro.game.objects.thing.character;

import java.util.Locale;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.logic.DamageCalculator;
import work.chiro.game.logic.attributes.dynamic.BasicDynamicAttributes;
import work.chiro.game.logic.attributes.dynamic.DynamicCharacterAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.logic.element.Element;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.objects.thing.attack.AbstractAttack;
import work.chiro.game.objects.thing.attack.UnderAttack;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.DelayTimer;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.ui.view.popup.DamagePopup;

/**
 * 抽象角色类
 */
public abstract class AbstractCharacter extends AbstractThing<BasicCharacterAttributes, AbstractAction>
        implements UnderAttack {
    /**
     * 元素战技对应的 DelayTimer
     */
    protected final DelayTimer skillAttackDelayTask = getDelayTimer();
    /**
     * 元素爆发对应的 DelayTimer
     */
    protected final DelayTimer chargedAttackDelayTask = getDelayTimer();
    /**
     * 角色的动态属性
     */
    protected final DynamicCharacterAttributes dynamicCharacterAttributes;

    /**
     * 获取新的 DelayTimer 对象
     *
     * @return DelayTimer
     */
    protected abstract DelayTimer getDelayTimer();

    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer, Vec2 sizeInit, Scale rotationInit, Scale alpha) {
        super(labelName, attributesClass, posInit, animateContainer, sizeInit, rotationInit, alpha);
        dynamicCharacterAttributes = DynamicCharacterAttributes.initFromCharacterAttributes(getBasicAttributes());
        skillAttackDelayTask.setValid();
        chargedAttackDelayTask.setValid();
        if (getBasicAttributes().isSizeAvailable()) {
            Utils.getLogger().debug("setSize: {}", getBasicAttributes().getSize());
            setSize(getBasicAttributes().getSize());
        }
    }

    public AbstractCharacter(String labelName, Class<BasicCharacterAttributes> attributesClass, Vec2 posInit, AbstractAction animateContainer) {
        this(labelName, attributesClass, posInit, animateContainer, null, null, null);
    }

    /**
     * 用于资源加载的临时类
     */
    public AbstractCharacter() {
        super();
        dynamicCharacterAttributes = null;
    }

    /**
     * 普攻
     */
    public void normalAttack() {
    }

    /**
     * 元素战技
     */
    public void skillAttack() {
    }

    /**
     * 元素爆发
     */
    public void chargedAttack() {
    }

    /**
     * 普攻前钩子
     */
    public void beforeNormalAttack() {
    }

    /**
     * 元素战技前钩子
     */
    public void beforeSkillAttack() {
    }

    /**
     * 元素爆发前钩子
     */
    public void beforeChargedAttack() {
    }

    public DynamicCharacterAttributes getDynamicCharacterAttributes() {
        return dynamicCharacterAttributes;
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "Character %s", getLabelName());
    }

    /**
     * 角色承受攻击伤害
     *
     * @param damage 伤害
     */
    protected void bearDamage(int damage) {
        Utils.getLogger().info("{} bearDamage: {}", this, damage);
        // 生成一个伤害 Popup
        Game.getInstance().getTopLayout().addView(new DamagePopup(getPosition().plus(new Vec2(0, 10)), Element.Pyro, damage));
        if (getHp() > damage) {
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

    /**
     * 绘制血条，绘制在 pos 对应 size 的正上方
     *
     * @param g          xGraphics
     * @param colorFront 前景色
     * @param colorBack  背景色
     * @param pos        中心
     * @param sizeUse    目标的 size
     * @param forceDraw  当血量等于最大血量的时候是否绘制
     */
    @SuppressWarnings("SameParameterValue")
    protected void drawHp(XGraphics g, int colorFront, int colorBack, Vec2 pos, Vec2 sizeUse, boolean forceDraw) {
        if (getHp() == getBasicAttributes().getMaxHp() && !forceDraw) {
            return;
        }
        int hpBarHeight = RunningConfig.drawHpBar;
        g.setColor(colorBack)
                .fillRect(pos.getX() - sizeUse.getX() / 2, pos.getY() - sizeUse.getY() / 2,
                        sizeUse.getX(), hpBarHeight);
        if (getBasicAttributes().getMaxHp() * 0.3 > getHp()) {
            g.setColor(DrawColor.red);
        } else {
            g.setColor(colorFront);
        }
        g.fillRect(pos.getX() - sizeUse.getX() / 2, pos.getY() - sizeUse.getY() / 2,
                sizeUse.getX() / getBasicAttributes().getMaxHp() * getHp(), hpBarHeight);
    }

    public void drawHp(XGraphics g, Vec2 pos, Vec2 size) {
        drawHp(g, DrawColor.red, DrawColor.gray, pos, size, true);
    }

    protected void drawHp(XGraphics g, int colorFront, int colorBack) {
        drawHp(g, colorFront, colorBack, getPosition(), getSize(), true);
    }

    protected void drawHp(XGraphics g) {
        drawHp(g, DrawColor.red, DrawColor.gray);
    }

    @Override
    @Deprecated
    public void draw(XGraphics g, boolean center) {
        super.draw(g, center);
    }

    public void drawWithoutHp(XGraphics g) {
        super.draw(g);
    }

    @Override
    public void draw(XGraphics g) {
        drawWithoutHp(g);
        drawHp(g);
    }

    @Override
    public AbstractCharacter getRelativeCharacter() {
        return this;
    }

    public boolean isEnemy() {
        return getDynamicCharacterAttributes().isEnemy();
    }

    @Override
    public BasicDynamicAttributes getBasicDynamicAttributes() {
        return getDynamicCharacterAttributes();
    }


}
