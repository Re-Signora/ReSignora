package work.chiro.game.x.compatible;

import work.chiro.game.vector.Vec2;

public abstract class XImage<T> {
    public abstract int getWidth();

    public abstract int getHeight();

    public abstract T getImage();

    public boolean isScaled() {
        return false;
    }

    public abstract int getPixel(Vec2 pos) throws ArrayIndexOutOfBoundsException;
}
