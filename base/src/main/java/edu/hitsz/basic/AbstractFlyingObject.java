package edu.hitsz.basic;

import edu.hitsz.utils.Utils;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.application.ImageManager;
import edu.hitsz.vector.Scale;
import edu.hitsz.vector.Vec;
import edu.hitsz.vector.Vec2;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    private final AnimateContainer animateContainer;
    private final Vec2 size;
    private final Scale rotation;

    /**
     * 图片,
     * null 表示未设置
     */
    protected BufferedImage image = null;

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
     * 有效（生存）标记，
     * 通常标记为 false的对象会再下次刷新时清除
     */
    protected boolean isValid = true;

    public AbstractFlyingObject(
            Vec2 posInit,
            AnimateContainer animateContainer,
            Vec2 sizeInit,
            Scale rotationInit) {
        this.position = posInit;
        this.animateContainer = animateContainer;
        this.size = sizeInit;
        this.rotation = rotationInit;
    }

    public AbstractFlyingObject(
            Vec2 posInit,
            AnimateContainer animateContainer,
            Vec2 sizeInit) {
        this(posInit, animateContainer, sizeInit, null);
    }

    public AbstractFlyingObject(
            Vec2 posInit,
            AnimateContainer animateContainer) {
        this(posInit, animateContainer, null, null);
    }

    public AbstractFlyingObject(Vec2 posInit) {
        this(posInit, new AnimateContainer(), null, null);
    }

    /**
     * 可飞行对象根据速度移动
     * 若飞行对象触碰到横向边界，横向速度反向
     */
    public void forward() {
        if (animateContainer.updateAll(Utils.getTimeMills())) {
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
    public boolean crash(AbstractFlyingObject abstractFlyingObject) {
        // 缩放因子，用于控制 y轴方向区域范围
        int factor = this instanceof AbstractAircraft ? 2 : 1;
        int fFactor = abstractFlyingObject instanceof AbstractAircraft ? 2 : 1;

        double x = abstractFlyingObject.getLocationX();
        double y = abstractFlyingObject.getLocationY();
        double fWidth = abstractFlyingObject.getWidth();
        double fHeight = abstractFlyingObject.getHeight();

        return x + (fWidth + this.getWidth()) / 2 > getLocationX()
                && x - (fWidth + this.getWidth()) / 2 < getLocationX()
                && y + (fHeight / fFactor + this.getHeight() / factor) / 2 > getLocationY()
                && y - (fHeight / fFactor + this.getHeight() / factor) / 2 < getLocationY();
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

    public BufferedImage getImage() {
        if (image == null) {
            image = ImageManager.get(this);
        }
        return image;
    }

    public double getWidth() {
        if (width == -1) {
            // 若未设置，则查询图片宽度并设置
            width = ImageManager.get(this).getWidth();
        }
        return width;
    }

    public double getHeight() {
        if (height == -1) {
            // 若未设置，则查询图片高度并设置
            height = ImageManager.get(this).getHeight();
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
    }

    public Vec2 getSize() {
        return size;
    }

    public Scale getRotation() {
        return rotation;
    }

    public void draw(Graphics g) {
        g.drawImage(getImage(),
                (int) (getLocationX() - image.getWidth() / 2), (int) (getLocationY() - image.getHeight() / 2),
                null);
    }
}

