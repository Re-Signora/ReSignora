package work.chiro.game.objects.thing.character;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.logic.DamageCalculator;
import work.chiro.game.logic.attributes.dynamic.BasicDynamicAttributes;
import work.chiro.game.logic.attributes.dynamic.DynamicCharacterAttributes;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.logic.buff.AbstractBuff;
import work.chiro.game.logic.buff.AbstractBuffFactory;
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
//    什么玩意儿？？？？

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
//        伤害计算
        attack.vanish();
//        子弹消失
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
//    画血条？？？？
    protected void drawHp(XGraphics g, int colorFront, int colorBack, Vec2 pos, Vec2 sizeUse, boolean forceDraw) {
        if (getHp() == getBasicAttributes().getMaxHp() && !forceDraw) {
//            什么情况会return啊，？？？？这个判断标准没看懂qwq
            return;
        }
        int hpBarHeight = RunningConfig.drawHpBar;
//        默认血条高度？？？？这是把所有偶读数据抽象到了一个类来总体控制嘛，所以每次就调用Runningconfig，但是为什么是5啊qwq
//        是我的错qwq，XGraphics居然是个系统类，呜呜，大意了，没有闪
        g.setColor(colorBack)
                .fillRect(pos.getX() - sizeUse.getX() / 2, pos.getY() - sizeUse.getY() / 2,
                        sizeUse.getX(), hpBarHeight);
        if (getBasicAttributes().getMaxHp() * 0.3 > getHp()) {
//            这是个什么玩意儿？
            g.setColor(DrawColor.red);
        } else {
            g.setColor(colorFront);
        }
//        这个是血量？
        g.fillRect(pos.getX() - sizeUse.getX() / 2, pos.getY() - sizeUse.getY() / 2,
                sizeUse.getX() / getBasicAttributes().getMaxHp() * getHp(), hpBarHeight);
    }

    public void drawHp(XGraphics g, Vec2 pos, Vec2 size) {
        drawHp(g, DrawColor.red, DrawColor.gray, pos, size, true);
//        forceDraw是要强撸嘛，这玩意儿有啥用？？？？
    }

    protected void drawHp(XGraphics g, int colorFront, int colorBack) {
        drawHp(g, colorFront, colorBack, getPosition(), getSize(), true);
    }

    protected void drawHp(XGraphics g) {
        drawHp(g, DrawColor.red, DrawColor.gray);
    }

//    这是因为有很多不同的输入所以要多态嘛，但是不是太多了点qwq，哦~有颜色问题qwq，不同颜色多态
    @Override
    @Deprecated
//    @Deprecated？？？？会被废弃？这个关键词没有看懂诶qwq
    public void draw(XGraphics g, boolean center) {
        super.draw(g, center);
    }

    public void drawWithoutHp(XGraphics g) {
        super.draw(g);
    }

    @Override
    public void draw(XGraphics g) {
//        我不是很理解，为什么呢这个draw WithoutHp画的是人，诶，虽然确实画的是不是血条的东西
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


//    add buff
    private List<AbstractBuff> buffList = new LinkedList<>();

    public void getBuff(AbstractBuffFactory abstractBuffFactory){
        String buffName=abstractBuffFactory.getName();
        boolean notUpdate=true;
        for (AbstractBuff abstractBuff:buffList){
            if (abstractBuff.getBuffName()==buffName) {
                abstractBuff.update();
                notUpdate=true;
            }
        }
        if (notUpdate) {
            buffList.add(abstractBuffFactory.create());
        }
    }
//  timer.TimeManager;
//    净化效果的remove？没想好，正常的remove应该是时间到了自动？？
    public void removeBuff (){

    }


}
