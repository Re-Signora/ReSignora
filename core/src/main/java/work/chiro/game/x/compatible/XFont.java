package work.chiro.game.x.compatible;

public class XFont<T> {
    protected T font;

    public XFont(T font) {
        this.font = font;
    }

    public T getFont() {
        return font;
    }
}
