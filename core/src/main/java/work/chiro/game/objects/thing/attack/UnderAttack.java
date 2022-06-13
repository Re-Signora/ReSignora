package work.chiro.game.objects.thing.attack;

public interface UnderAttack {
    void applyAttack(AbstractAttack attack);
    boolean isCrashAttack(AbstractAttack attack);
}
