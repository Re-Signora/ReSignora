package work.chiro.game.prop;

import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.Game;
import work.chiro.game.vector.Vec2;

/**
 * @author Chiro
 */
public class BulletProp extends AbstractProp {
    public BulletProp(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    @Override
    public AbstractProp update() {
        playSupplyMusic();
        System.out.println("FireSupply active!");
        HeroAircraftFactory.getInstance().increaseShootNum();
        Game.getThreadFactory().newThread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            } finally {
                HeroAircraftFactory.getInstance().decreaseShootNum();
            }
        }).start();
        return this;
    }
}
