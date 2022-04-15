package edu.hitsz.prop;

/**
 * 道具工厂接口
 * @author Chiro
 */
public abstract class AbstractPropFactory {
    protected int locationX, locationY;

    public AbstractPropFactory(int locationX, int locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
    }
    /**
     * 创建新的道具
     * @return 创建的道具对象
     */
    abstract AbstractProp create();
}
