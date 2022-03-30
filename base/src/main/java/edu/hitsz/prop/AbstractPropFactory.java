package edu.hitsz.prop;

/**
 * 道具工厂接口
 * @author Chiro
 */
public interface AbstractPropFactory {
    /**
     * 创建新的道具
     * @return 创建的道具对象
     */
    AbstractProp create();
}
