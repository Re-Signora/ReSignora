package work.chiro.game.objects.bullet;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.vector.Vec2;

/**
 * @author hitsz
 */
public class EnemyBullet extends BaseBullet {
    public EnemyBullet(Vec2 posInit, AnimateContainer animateContainer, int power) {
        super(posInit, animateContainer, power);
    }
}
