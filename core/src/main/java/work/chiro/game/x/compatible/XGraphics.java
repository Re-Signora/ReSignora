package work.chiro.game.x.compatible;

import java.util.List;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.objects.background.AbstractBackground;
import work.chiro.game.x.ui.XLayout;

public abstract class XGraphics {
    protected double alpha = 1.0;
    protected double rotation = 0.0;
    protected int color = 0x0;

    /**
     * 在 (x, y) 绘制图片
     *
     * @param image 图片
     * @param x     X坐标
     * @param y     Y坐标
     * @return this
     */
    public abstract XImage<?> drawImage(XImage<?> image, double x, double y);

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
    public abstract XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h);

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
                    view.draw(this);
                }
            });
        }

        // 绘制物体
        sortedThings.forEach(thing -> {
            synchronized (thing) {
                thing.draw(this);
            }
        });

        // 绘制本层背景
        AbstractBackground background = game.getBackgrounds().get(1);
        if (background != null) background.draw(this);
        // 绘制本层的 UI
        game.getActivityManager().getTopLayout().forEach(view -> {
            synchronized (view) {
                view.draw(this);
            }
        });
    }

    private XFont<?> font = null;

    // override this to apply font
    public XGraphics setFont(XFont<?> font) {
        this.font = font;
        return this;
    }

    public XFont<?> getFont() {
        return font;
    }

    static private float canvasScale = 1.0f;
    static private final int fontSize = 20;

    public static float getCanvasScale() {
        return canvasScale;
    }

    public static void setCanvasScale(float canvasScale) {
        XGraphics.canvasScale = canvasScale;
    }

    public static int getFontSize() {
        return fontSize;
    }

    public void paintInfo(Game game) {
        int d = (int) (getFontSize() * 3.0f / getCanvasScale());
        int x = 10;
        int y = d;
        setFont(ResourceProvider.getInstance().getFont("genshin"));
        setColor(0xcfcfcfcf);
        setAlpha(0.8);
        drawString("SCORE:" + (int) (RunningConfig.score), x, y);
        y = y + d;
        drawString("FPS:" + game.getTimerController().getFps(), x, y);
    }
}
