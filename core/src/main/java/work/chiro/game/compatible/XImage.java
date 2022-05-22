package work.chiro.game.compatible;

public abstract class XImage<T> {
    public abstract int getWidth();

    public abstract int getHeight();

    public abstract T getImage();

    public boolean isScaled() {
        return false;
    }
}
