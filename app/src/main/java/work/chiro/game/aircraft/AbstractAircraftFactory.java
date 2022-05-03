package work.chiro.game.aircraft;

import work.chiro.game.config.AbstractConfig;

/**
 * @author Chiro
 */
public interface AbstractAircraftFactory {
    /**
     * 飞机类的抽象工厂接口
     *
     * @param config 当前配置
     * @return 创建的产品对象
     */
    AbstractAircraft create(AbstractConfig config);
}
