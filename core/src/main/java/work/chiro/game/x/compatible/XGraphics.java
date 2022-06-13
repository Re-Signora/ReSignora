package work.chiro.game.x.compatible;

import java.util.List;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.objects.background.AbstractBackground;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.colors.CoupleColor;
import work.chiro.game.x.compatible.colors.DrawColor;
import work.chiro.game.x.ui.layout.XLayout;
import work.chiro.game.x.ui.view.XView;

public abstract class XGraphics {
    protected double alpha = 1.0;
    protected double rotation = 0.0;
    protected int color = DrawColor.black;
    protected int colorBoarder = DrawColor.white;

    /**
     * 在 (x, y) 绘制图片
     *
     * @param image 图片
     * @param x     X坐标
     * @param y     Y坐标
     * @return this
     */
    public XImage<?> drawImage(XImage<?> image, double x, double y) {
        return drawImage(image, x, y, false);
    }

    public abstract XImage<?> drawImage(XImage<?> image, double x, double y, boolean flipped);

    /**
     * 在 (x, y) 绘制大小为 (w, h) 的图片
     *
     * @param image 图片
     * @param x     X坐标
     * @param y     Y坐标
     * @param w     宽度
     * @param h     高度
     * @return this
     */
    public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h) {
        return drawImage(image, x, y, w, h, false);
    }

    public abstract XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h, boolean flipped);

    /**
     * 设置绘制图片时的透明度
     *
     * @param alpha 透明度 (0~1)
     * @return this
     */
    public abstract XGraphics setAlpha(double alpha);

    /**
     * 设置绘制图片时的旋转角度
     *
     * @param rotation 旋转角度
     * @return this
     */
    public abstract XGraphics setRotation(double rotation);

    /**
     * 设置绘制图形颜色
     *
     * @param color 颜色
     * @return this
     */
    public abstract XGraphics setColor(int color);

    public XGraphics setBoarderColor(int colorBoarder) {
        this.colorBoarder = colorBoarder;
        return this;
    }

    public XGraphics applyCoupleColor(CoupleColor coupleColor) {
        setColor(coupleColor.primary);
        setBoarderColor(coupleColor.secondary);
        return this;
    }

    /**
     * 绘制一个填充矩形
     *
     * @param x      X坐标
     * @param y      Y坐标
     * @param width  宽度
     * @param height 高度
     * @return this
     */
    public abstract XGraphics fillRect(double x, double y, double width, double height);

    /**
     * 绘制文字
     *
     * @param text 文字
     * @param x    X坐标
     * @param y    Y坐标
     * @return this
     */
    public abstract XGraphics drawString(String text, double x, double y);

    public XGraphics drawString(String text, Vec2 pos) {
        return drawString(text, pos.getX(), pos.getY());
    }

    abstract public XGraphics drawUIString(XView view, String text);

    public XGraphics drawBoarderString(Vec2 position, String text) {
        return drawBoarderString(position, null, text);
    }

    abstract public XGraphics drawBoarderString(Vec2 position, Vec2 size, String text);

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void paintInOrdered(Game game) {
        List<AbstractFlyingObject<?>> sortedThings = game.getSortedThings();

        // 绘制上层背景
        AbstractBackground lastBackground = game.getBackgrounds().get(0);
        if (lastBackground != null) lastBackground.draw(this);
        // 绘制上一层的 UI
        XLayout secondaryLayout = game.getActivityManager().getSecondaryLayout();
        if (secondaryLayout != null) {
            secondaryLayout.forEach(view -> {
                synchronized (view) {
                    if (view.isValid()) view.draw(this);
                }
            });
        }

        // 绘制本层背景
        AbstractBackground background = game.getBackgrounds().get(1);
        if (background != null) background.draw(this);

        // 绘制物体
        sortedThings.forEach(thing -> {
            synchronized (thing) {
                if (thing.isValid()) thing.draw(this);
            }
        });

        // 绘制本层的 UI
        game.getActivityManager().getTopLayout().forEach(view -> {
            synchronized (view) {
                if (view.isValid()) view.draw(this);
            }
        });

        // 删除 views
        game.removeInvisibleViews();
    }

    private XFont<?> font = null;

    // override this to apply font
    public XGraphics setFont(XFont<?> font) {
        this.font = font;
        return this;
    }

    public XGraphics setFont(String name) {
        return setFont(ResourceProvider.getInstance().getFont(name, getFontSize()));
    }

    public XFont<?> getFont() {
        if (font == null) {
            font = ResourceProvider.getInstance().getFont("main", getFontSize());
        }
        return font;
    }

    static private float canvasScale = 1.0f;
    static private final double fontSize = 56;

    public static float getCanvasScale() {
        return canvasScale;
    }

    public static void setCanvasScale(float canvasScale) {
        XGraphics.canvasScale = canvasScale;
    }

    public double getFontSize() {
        return fontSize;
    }

    public void paintInfo(Game game) {
        int d = (int) (getFontSize() / getCanvasScale());
        int x = 10;
        int y = d;
        setFont("genshin");
        setColor(0xcfcfcfcf);
        setAlpha(0.8);
        drawString("SCORE:" + (int) (RunningConfig.score), x, y);
        y = y + d;
        drawString("FPS:" + game.getTimerController().getFps(), x, y);
    }

    /**
     * 绘制一个以 (x, y) 为中心、(r1, r2) 为半径的椭圆
     *
     * @param x  x 坐标
     * @param y  y 坐标
     * @param r1 r1 半径
     * @param r2 r2 半径
     * @return this
     */
    abstract public XGraphics ellipse(double x, double y, double r1, double r2);

    public XGraphics ellipse(Vec2 pos, Vec2 r) {
        return ellipse(pos.getX(), pos.getY(), r.getX(), r.getY());
    }

    /**
     * 绘制一个以 (x, y) 为圆心、r 为半径的圆
     *
     * @param x x 坐标
     * @param y y 坐标
     * @param r r 半径
     * @return this
     */
    abstract public XGraphics circle(double x, double y, double r);

    public XGraphics circle(Vec2 pos, double r) {
        return circle(pos.getX(), pos.getY(), r);
    }

    abstract public XImage<?> resizeImage(XImage<?> image, double w, double h);

    public XImage<?> resizeImage(XImage<?> image) {
        return resizeImage(image, 0, 0);
    }
}
