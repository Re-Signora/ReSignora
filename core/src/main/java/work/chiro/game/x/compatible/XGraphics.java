package work.chiro.game.x.compatible;

import java.util.ArrayList;
import java.util.List;

import work.chiro.game.game.Game;
import work.chiro.game.objects.AbstractFlyingObject;
import work.chiro.game.utils.Utils;

public abstract class XGraphics {
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
    public List<AbstractFlyingObject> getSortedFlyingObjects(Game game) {
        List<List<? extends AbstractFlyingObject>> allFlyingObjects = game.getAllFlyingObjects();
        List<AbstractFlyingObject> sortedFlyingObjects = new ArrayList<>();
        synchronized (allFlyingObjects) {
            allFlyingObjects.forEach(sortedFlyingObjects::addAll);
        }
        Utils.getLogger().debug("before sort: {}", sortedFlyingObjects);
        sortedFlyingObjects.sort((a, b) -> (a.getAnchor().getY() >= b.getAnchor().getY()) ? (a.getAnchor().getY() == b.getAnchor().getY() ? 0 : 1) : -1);
        Utils.getLogger().debug("after sort: {}", sortedFlyingObjects);
        return sortedFlyingObjects;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void paintInOrdered(Game game) {
        List<AbstractFlyingObject> sortedFlyingObjects = getSortedFlyingObjects(game);

        // 绘制背景
        game.getBackgrounds().forEach(background -> {
            synchronized (background) {
                background.draw(this);
            }
        });

        // 绘制飞行的物体
        sortedFlyingObjects.forEach(flyingObject -> {
            synchronized (flyingObject) {
                flyingObject.draw(this);
            }
        });

        // 绘制 UI
        game.getLayout().forEach(view -> {
            synchronized (view) {
                view.draw(this);
            }
        });
    }
}
