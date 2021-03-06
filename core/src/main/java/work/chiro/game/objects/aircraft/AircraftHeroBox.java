package work.chiro.game.objects.aircraft;

import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.vector.Vec2;

/**
 * 绘制英雄判定框
 *
 * @author Chiro
 */
public class AircraftHeroBox extends AircraftBox {
    public AircraftHeroBox(Vec2 posInit, Vec2 sizeInit) {
        super(posInit, sizeInit);
    }

    @Override
    public void draw(XGraphics g) {
        super.draw(g);
    }
}
