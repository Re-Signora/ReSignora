package work.chiro.game.prop;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.GameWindow;
import work.chiro.game.application.MusicManager;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BombProp extends AbstractProp {
    public BombProp(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    @Override
    public AbstractProp update() {
        System.out.println("BombSupply active!");
        Utils.startMusic(MusicManager.MusicType.BOMB_EXPLOSION);
        for (AbstractAircraft enemy : enemyAircrafts) {
            GameWindow.getInstance().getGame().increaseScore(enemy.getScore());
            enemy.vanish();
        }
        return this;
    }
}
