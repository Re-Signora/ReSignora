package work.chiro.game.logic.attributes;

import work.chiro.game.vector.Vec2;

/**
 * Thing 属性基类，使用独立于 AbstractObjects 的属性，减少继承造成的数据耦合关系
 */
public class BasicThingAttributes {
    /**
     * 是否可碰撞
     */
    public boolean collision = true;
    /**
     * 持续存在时间(秒)，0 表示一直存在
     */
    public double duration = 0;
    /**
     * 移动速度
     */
    public int speed = 10;
    /**
     * 宽度
     */
    public double width = 0;
    /**
     * 高度
     */
    public double height = 0;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isSizeAvailable() {
        return width != 0 && height != 0;
    }

    public Vec2 getSize() {
        return new Vec2(width, height);
    }
}
