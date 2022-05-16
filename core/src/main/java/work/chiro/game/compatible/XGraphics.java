package work.chiro.game.compatible;

public interface XGraphics {
    XGraphics drawImage(XImage<?> image, double x, double y);
    XGraphics setAlpha(double alpha);
    XGraphics setRotation(double rotation);
}
