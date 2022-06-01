package work.chiro.game.x.compatible;

import java.util.Locale;

import work.chiro.game.vector.Vec2;

public abstract class XImage<T> {
    abstract public String getName();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract T getImage();

    public boolean isScaled() {
        return false;
    }

    public abstract int getPixel(Vec2 pos) throws ArrayIndexOutOfBoundsException;

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "im(name=%s, size=[%d, %d], scaled=%s)", getName(), getWidth(), getHeight(), isScaled());
    }
}
