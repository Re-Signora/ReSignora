package work.chiro.game.logic.buff;

import work.chiro.game.game.Game;
import work.chiro.game.logic.attributes.BasicThingAttributes;
import work.chiro.game.logic.element.Element;
import work.chiro.game.objects.thing.character.AbstractCharacter;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.ui.view.popup.DamagePopup;

/**
 * @author uruom
 * @version 1.0
 * @ClassName FiringBuff
 * @date 2022/7/1 11:23
 */
//1.灼烧之火：当在灼烧之火持续时间内，若受到伤害，则承受下一次攻击伤害50%的火焰伤害。
// 灼烧之火持续时间5s，灼烧触发后灼烧状态消失（技能特殊说明灼烧附着除外）。
// 在灼烧之火持续时间内，所受到的火属性伤害增加至300%（最后的灼烧伤害是在持续完后发生，所以不计入300%的加成）。
// eg：若在灼烧期间，受到了一次为100的伤害，则在该伤害结算后，再次受到灼烧带来的50点火焰伤害。

public class FiringBuff extends AbstractBuff{

    @Override
    public void onStart() {
        super.onStart();
        buffName="Firing";
    }

    @Override
    public BasicThingAttributes calc(BasicThingAttributes a) {
        return super.calc(a);
    }

    @Override
    public double getDuration() {
        return super.getDuration();
    }

    @Override
    public String getBuffName() {
        return super.getBuffName();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
//    我觉得这个地方应该放一个图片展示，但是我不是很知道这个的图片是在哪个地方展示的，却game看一下qwq


    @Override
    public int value(Element damageType, int damage, AbstractCharacter abstractCharacter) {
//        伤害增益
        if (damageType== Element.Pyro) {
            damage*=3;
        }
//        灼烧伤害
        int firingDamage=damage/2;
        Game.getInstance().getTopLayout().addView(new DamagePopup(abstractCharacter.getPosition().plus(new Vec2(20, 10)), Element.Pyro, firingDamage,new Vec2(1,1)));

        if (abstractCharacter.getHp() > firingDamage) {
            abstractCharacter.getDynamicCharacterAttributes().setHp(abstractCharacter.getHp() - firingDamage);
        } else {
            abstractCharacter.getDynamicCharacterAttributes().setHp(0);
        }
        return damage;
    }
}
