package work.chiro.game.objects;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.callback.BasicCallback;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;

/**
 * 可飞行对象的父类
 *
 * @author hitsz
 */
public abstract class AbstractObject<A extends AnimateContainer> {

    public Vec2 getPosition() {
        return position;
    }

    /**
     * 物体当前位置
     */
    private final Vec2 position;

    /**
     * 动画容器
     */
    private final A animateContainer;
    /**
     * 大小
     */
    private Vec2 size;
    /**
     * 旋转角度（弧度制）
     */
    private final Scale rotation;
    /**
     * 透明度 (0~1)
     */
    private Scale alpha;

    /**
     * 图片,
     * null 表示未设置
     */
    protected XImage<?> image = null;

    /**
     * x 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    protected double width = -1;

    /**
     * y 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    protected double height = -1;

    /**
     * 是否为无敌状态
     */
    private boolean invincible = false;

    /**
     * 有效（生存）标记，
     * 通常标记为 false的对象会再下次刷新时清除
     */
    protected boolean isValid = true;
    /**
     * 缓存的图片
     */
    protected Map<XImage<?>, XImage<?>> cachedImage = new HashMap<>();
    /**
     * 图片是否左右翻转
     */
    protected boolean flipped = false;

    public AbstractObject(
            Vec2 posInit,
            A animateContainer,
            Vec2 sizeInit,
            Scale rotationInit,
            Scale alpha) {
        this.position = posInit;
        this.animateContainer = animateContainer;
        this.size = sizeInit;
        this.rotation = updateRotation(rotationInit);
        this.alpha = alpha == null ? new Scale(1.0) : alpha;
    }

    public AbstractObject(
            Vec2 posInit,
            A animateContainer,
            Vec2 sizeInit,
            Scale rotationInit) {
        this(posInit, animateContainer, sizeInit, rotationInit, null);
    }

    public AbstractObject(
            Vec2 posInit,
            A animateContainer,
            Vec2 sizeInit) {
        this(posInit, animateContainer, sizeInit, null, null);
    }

    public AbstractObject(
            Vec2 posInit,
            A animateContainer) {
        this(posInit, animateContainer, null, null, null);
    }

    public AbstractObject(
            Vec2 posInit,
            A animateContainer,
            Scale alpha) {
        this(posInit, animateContainer, null, null, alpha);
    }

    protected Boolean checkInBoundary() {
        return !(getLocationX() > RunningConfig.windowWidth ||
                getLocationX() < 0 ||
                getLocationY() > RunningConfig.windowHeight ||
                getLocationY() < 0);
    }

    /**
     * 可飞行对象根据速度移动
     * 若飞行对象触碰到横向边界，横向速度反向
     */
    public void forward() {
        if (animateContainer.updateAll(TimeManager.getTimeMills()) || !checkInBoundary()) {
            vanish();
        }
    }

    /**
     * 碰撞检测，当对方坐标进入我方范围，判定我方击中<br>
     * 对方与我方覆盖区域有交叉即判定撞击。
     * <br>
     * 非飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/2, y + height/2]
     * <br>
     * 飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/4, y + height/4]
     *
     * @param abstractObject 撞击对方
     * @return true: 我方被击中; false 我方未被击中
     */
    public boolean crash(AbstractObject<?> abstractObject, double locationX, double locationY, double width, double height) {
        if (isInvincible() || abstractObject.isInvincible()) {
            return false;
        }
        // 缩放因子，用于控制 y轴方向区域范围
//        int factor = this instanceof AbstractAircraft ? 2 : 1;
//        int fFactor = abstractObject instanceof AbstractAircraft ? 2 : 1;
        int factor = 1;
        int fFactor = 1;

        double x = abstractObject.getLocationX();
        double y = abstractObject.getLocationY();
        double fWidth = abstractObject.getWidth();
        double fHeight = abstractObject.getHeight();

        double w = (fWidth + width);
        double h = (fHeight / fFactor + height / factor) / 2;
        return x + w / 2 > locationX && x - w / 2 < locationX
                && y + h > locationY && y - h < locationY;
    }

    public boolean crash(AbstractObject<?> abstractObject) {
        return crash(abstractObject, getLocationX(), getLocationY(), getWidth(), getHeight());
    }

    public double getLocationX() {
        return getPosition().getX();
    }

    public double getLocationY() {
        return getPosition().getY();
    }

    public void setPosition(double x, double y) {
        this.position.set(x, y);
    }

    public void setPosition(Vec2 posNew) {
        this.position.set(posNew);
    }

    public Vec getSpeed() {
        return animateContainer.getSpeed(TimeManager.getTimeMills());
    }

    public double getSpeedY() {
        return getSpeed().get().get(1);
    }

    public XImage<?> getImage() {
        return getImage(false);
    }

    public XImage<?> getImage(boolean getRawImage) {
        if (image == null) {
            try {
                String filename = getImageFilename();
                if (filename == null) {
                    image = ImageManager.getInstance().get(this.getClass());
                } else {
                    image = Utils.getCachedImageFromResource(filename);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        if (keepImage()) {
            if (cachedImage.containsKey(image) && !getRawImage && RunningConfig.enableImageCache) {
                return cachedImage.get(image);
            }
            return image;
        } else {
            XImage<?> loadedImage = image;
            image = null;
            return loadedImage;
        }
    }

    public double getWidth() {
        if (getSize() != null) {
            width = getSize().getX();
            return width;
        }
        if (width == -1) {
            // 若未设置，则查询图片宽度并设置
            if (keepImage()) {
                width = getImage().getWidth();
            } else {
                return getImage().getWidth();
            }
        }
        return width;
    }

    public double getHeight() {
        if (getSize() != null) {
            height = getSize().getY();
            return height;
        }
        if (height == -1) {
            // 若未设置，则查询图片高度并设置
            if (keepImage()) {
                height = getImage().getHeight();
            } else {
                return getImage().getHeight();
            }
        }
        return height;
    }

    public boolean isValid() {
        return this.isValid;
    }

    /**
     * 标记消失，
     * isValid = false.
     * notValid() => true.
     */
    public void vanish() {
        isValid = false;
        if (onVanish != null) {
            onVanish.run();
        }
        // Game.getInstance().getTimerController().remove(getClass());
        Game.getInstance().getTimerController().remove(this);
    }

    public Vec2 getSize() {
        return size;
    }

    public void setSize(Vec2 size) {
        if (this.size != null)
            this.size.set(size);
        else
            this.size = size;
    }

    public Scale updateRotation() {
        if (animateContainer != null)
            return animateContainer.getRotation();
        return new Scale();
    }

    public Scale updateRotation(Scale rotationNew) {
        if (rotationNew == null) {
            return updateRotation();
        } else {
            return rotationNew;
        }
    }

    public Scale getRotation(boolean update) {
        if (update) {
            rotation.set(updateRotation());
        }
        return rotation;
    }

    public Scale getRotation() {
        return getRotation(false);
    }

    public void draw(XGraphics g, boolean center) {
        XImage<?> newImage = g.setAlpha(getAlpha().getX())
                .setRotation(getRotation(true).getX())
                .drawImage(getImage(),
                        (getLocationX() - (center ? getWidth() / 2 : 0)),
                        getLocationY() - (center ? getHeight() / 2 : 0),
                        getWidth(), getHeight(), isFlipped());
//                        20, 20, isFlipped());
        if (!cachedImage.containsValue(newImage) && RunningConfig.modePC) {
            cachedImage.put(getImage(true), newImage);
        }
    }

    public void draw(XGraphics g) {
        draw(g, true);
    }

    protected BasicCallback onVanish = null;

    public void setOnVanish(BasicCallback onVanish) {
        this.onVanish = onVanish;
    }

    /**
     * 获取当前对象的图像文件名（仅名称不需要路径）
     *
     * @return 文件名
     */
    protected String getImageFilename() {
        return null;
//        但是为什么出来是空啊qwq，是怎么进去的啊？？？？
    }

    /**
     * 是否将图像缓存在本类中
     *
     * @return 是否缓存
     */
    protected Boolean keepImage() {
        return true;
    }

    public A getAnimateContainer() {
        return animateContainer;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public Scale getAlpha() {
        return alpha;
    }

    public void setAlpha(Scale alpha) {
        this.alpha = alpha;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public boolean isFlipped() {
        return flipped;
    }
}

