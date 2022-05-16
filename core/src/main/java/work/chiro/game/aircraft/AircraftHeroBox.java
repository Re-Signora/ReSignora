package work.chiro.game.aircraft;

import work.chiro.game.compatible.XGraphics;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.vector.Vec2;

/**
 * 绘制英雄判定框
 *
 * @author Chiro
 */
public class AircraftHeroBox extends AircraftBox {
    public AircraftHeroBox(AbstractConfig config, Vec2 posInit, Vec2 sizeInit) {
        super(config, posInit, sizeInit);
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
    }
}
