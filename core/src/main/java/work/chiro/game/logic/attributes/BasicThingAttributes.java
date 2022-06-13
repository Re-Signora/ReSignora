package work.chiro.game.logic.attributes;

import work.chiro.game.vector.Vec2;

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
     * 宽度
     */
    public double width = 0;
    /**
     * 高度
     */
    public double height = 0;

    public boolean sizeAvailable() {
        return width != 0 && height != 0;
    }

    public Vec2 getSize() {
        return new Vec2(width, height);
    }
}
