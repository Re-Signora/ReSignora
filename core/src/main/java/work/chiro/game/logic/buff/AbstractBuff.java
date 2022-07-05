package work.chiro.game.logic.buff;



import work.chiro.game.logic.attributes.BasicThingAttributes;
import work.chiro.game.logic.element.Element;
import work.chiro.game.objects.thing.character.AbstractCharacter;

//工厂模式？？

//1.灼烧之火：当在灼烧之火持续时间内，若受到伤害，则承受下一次攻击伤害50%的火焰伤害。灼烧之火持续时间5s，灼烧触发后灼烧状态消失（技能特殊说明灼烧附着除外）。在灼烧之火持续时间内，所受到的火属性伤害增加至300%（最后的灼烧伤害是在持续完后发生，所以不计入300%的加成）。eg：若在灼烧期间，受到了一次为100的伤害，则在该伤害结算后，再次受到灼烧带来的50点火焰伤害。
//
//2.冻结之冰：当在冻结之冰持续时间内，若受到水属性/冰属性伤害时，将会被冻结3s。冻结期间无法行动，若在冻结期间受到非水属性/冰属性伤害时，冻结破碎且该伤害提升至200%。冻结之冰持续时间为5s，冻结触发后冻结之冰状态消失。在冻结之冰持续时间内，所受到的冰属性伤害增加至300%。
//
//3.迟滞之水：当在迟滞之水持续期间，普通攻击冷却时间增至200%，（攻速减半），若在迟滞止水附着时间内使用技能/道具/魔偶召唤，则该技能/道具/魔偶冷却时间增加至150%，且不随迟滞之水消失而恢复时间。迟滞之水持续时间为5s，不可叠加，当自身本身处于迟滞之水状态又受到迟滞之水附着时，刷新迟滞之水时间。迟滞之水持续时间内，所受到的水属性伤害增加至150%。
//
//4.嗜能之雷：在嗜能之雷持续时间内，每秒减少自身5点灵魂碎片与5点元素能量，若元素能量/灵魂碎片不够衰减，则造成（灵魂碎片缺失量+元素能量缺失量）*自身攻击力点雷属性伤害。嗜能之雷持续时间为5s，不可叠加。当自身本身处于嗜能之雷状态又受到嗜能之雷附着时，刷新嗜能之雷时间。嗜能之雷持续时间，所受到的雷属性伤害增加至150%。
//
//5.阻塞之风：在阻塞之风持续时间内，自身移动速度减半，技能吟唱速度增减为200%，且若在吟唱时间内受到任意伤害，则技能将会被打断。阻塞之风持续时间5s。当自身本身处于阻塞之风状态又受到阻塞之风附着时，刷新阻塞之风时间。阻塞之风持续时间内，所受到的风属性伤害增加至150%
//
//6.生机之草：在生机之草生效期间，清除并免疫自身所有负面效果。若在生机之草持续时间内没有受到任何伤害，则在生机之草结束后，恢复自身当前血量的10%。（最后的恢复不享受治疗加成）。生机之草持续时间5s。
//
//7.千嶂之岩：在千嶂之岩持续时间内，岩属性伤害降低至50%，并使得自身受到的其他伤害减少至80%。当以此方法免疫超过自身最大血量10%的伤害时，千嶂之岩消失。千嶂之岩最大持续时间8s。
public abstract class AbstractBuff {
    String buffName;
    int numberOfBuff;
    public void onStart() {

    }


    public double getDuration() {
        return 0;
    }

//    这个有什么用啊？
    public BasicThingAttributes calc(BasicThingAttributes a) {
        return a;
    }

    public String getBuffName(){return buffName;}

//    标记？
    public void imageShow(){}

//    Buff效果
    public int value(Element damageType, int damage, AbstractCharacter abstractCharacter){return damage;}

    public void onStop() {
    }

//    当自身本身有这个buff 的时候，buff更新/若有的话层数增加一层
    public void update(){
//    重新计时，但是我没有计时器啊qwq
//        层数++？
    }
}
