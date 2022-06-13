package work.chiro.game.objects.thing.attack;

import work.chiro.game.objects.thing.character.AbstractCharacter;

public interface UnderAttack {
    void applyAttack(AbstractAttack attack);
    boolean isCrashAttack(AbstractAttack attack);
    AbstractCharacter getRelativeCharacter();
}
