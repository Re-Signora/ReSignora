package work.chiro.game.basic;

import java.io.IOException;

import work.chiro.game.aircraft.AbstractAircraft;
import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.compatible.XGraphics;
import work.chiro.game.compatible.XImage;
import work.chiro.game.config.AbstractConfig;
import work.chiro.game.config.Constants;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Scale;
import work.chiro.game.vector.Vec;
import work.chiro.game.vector.Vec2;

/**
 * 可飞行对象的父类
 *
 * @author hitsz
 */
public abstract class AbstractFlyingObject {

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
    private final AnimateContainer animateContainer;
    /**
     * 大小
     */
    private final Vec2 size;
    /**
     * 旋转角度（弧度制）
     */
    private final Scale rotation;
    /**
     * 透明度 (0~1)
     */
    private final Scale alpha;

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
     * 此物体适用的配置信息
     */
    final protected AbstractConfig config;

    /**
     * 是否为无敌状态
     */
    private boolean invincible = false;


    /**
     * 有效（生存）标记，
     * 通常标记为 false的对象会再下次刷新时清除
     */
    protected boolean isValid = true;

    public AbstractFlyingObject(
            AbstractConfig config,
            Vec2 posInit,
            AnimateContainer animateContainer,
            Vec2 sizeInit,
            Scale rotationInit,
            Scale alpha) {
        this.config = config;
        this.position = posInit;
        this.animateContainer = animateContainer;
        this.size = sizeInit;
        this.rotation = updateRotation(rotationInit);
        this.alpha = alpha == null ? new Scale(1.0) : alpha;
    }

    public AbstractFlyingObject(
            AbstractConfig config,
            Vec2 posInit,
            AnimateContainer animateContainer,
            Vec2 sizeInit,
            Scale rotationInit) {
        this(config, posInit, animateContainer, sizeInit, rotationInit, null);
    }

    public AbstractFlyingObject(
            AbstractConfig config,
            Vec2 posInit,
            AnimateContainer animateContainer,
            Vec2 sizeInit) {
        this(config, posInit, animateContainer, sizeInit, null, null);
    }

    public AbstractFlyingObject(
            AbstractConfig config,
            Vec2 posInit,
            AnimateContainer animateContainer) {
        this(config, posInit, animateContainer, null, null, null);
    }

    public AbstractFlyingObject(
            AbstractConfig config,
            Vec2 posInit,
            AnimateContainer animateContainer,
            Scale alpha) {
        this(config, posInit, animateContainer, null, null, alpha);
    }

    public AbstractFlyingObject(
            AbstractConfig config,
            Vec2 posInit) {
        this(config, posInit, new AnimateContainer(), null, null, null);
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
        if (animateContainer.updateAll(Utils.getTimeMills()) || !checkInBoundary()) {
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
     * @param abstractFlyingObject 撞击对方
     * @return true: 我方被击中; false 我方未被击中
     */
    public boolean crash(AbstractFlyingObject abstractFlyingObject, double locationX, double locationY, double width, double height) {
        if (isInvincible() || abstractFlyingObject.isInvincible()) {
            return false;
        }
        // 缩放因子，用于控制 y轴方向区域范围
        int factor = this instanceof AbstractAircraft ? 2 : 1;
        int fFactor = abstractFlyingObject instanceof AbstractAircraft ? 2 : 1;

        double x = abstractFlyingObject.getLocationX();
        double y = abstractFlyingObject.getLocationY();
        double fWidth = abstractFlyingObject.getWidth();
        double fHeight = abstractFlyingObject.getHeight();

        double w = (fWidth + width);
        double h = (fHeight / fFactor + height / factor) / 2;
        return x + w / 2 > locationX && x - w / 2 < locationX
                && y + h > locationY && y - h < locationY;
    }

    public boolean crash(AbstractFlyingObject abstractFlyingObject) {
        return crash(abstractFlyingObject, getLocationX(), getLocationY(), getWidth(), getHeight());
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

    public Vec getSpeed() {
        return animateContainer.getSpeed(Utils.getTimeMills());
    }

    public double getSpeedY() {
        return getSpeed().get().get(1);
    }

    public XImage<?> getImage() {
        if (image == null) {
            try {
                String filename = getImageFilename();
                if (filename == null) {
                    image = ImageManager.getInstance().get(this);
                } else {
                    image = Utils.getCachedImage(filename);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        if (keepImage()) {
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
            height = getSize().getX();
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

    public boolean notValid() {
        return !this.isValid;
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
    }

    public Vec2 getSize() {
        return size;
    }

    public Scale updateRotation() {
        return animateContainer.getRotation();
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
        g.setAlpha(getAlpha().getX())
                .setRotation(getRotation(true).getX())
                .drawImage(getImage(), (getLocationX() - (center ? getWidth() / 2 : 0)),
                getLocationY() - (center ? getHeight() / 2 : 0));
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
    }

    /**
     * 是否将图像缓存在本类中
     *
     * @return 是否缓存
     */
    protected Boolean keepImage() {
        return true;
    }

    public AnimateContainer getAnimateContainer() {
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
}

