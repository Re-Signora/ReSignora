package work.chiro.game.objects.thing.attack;

import work.chiro.game.objects.thing.character.AbstractCharacter;

/**
 * 受攻击接口
 */
public interface UnderAttack {
    /**
     * 处理一个攻击
     *
     * @param attack attack
     */
    void applyAttack(AbstractAttack attack);

    /**
     * 处理是否和攻击相碰撞
     *
     * @param attack attack
     * @return 是否碰撞
     */
    boolean isCrashAttack(AbstractAttack attack);

    /**
     * 获取受攻击对应的角色
     *
     * @return 角色
     */
    AbstractCharacter getRelativeCharacter();
}
