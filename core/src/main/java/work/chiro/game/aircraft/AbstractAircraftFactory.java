package work.chiro.game.aircraft;

/**
 * @author Chiro
 */
public interface AbstractAircraftFactory {
    /**
     * 飞机类的抽象工厂接口
     *
     * @return 创建的产品对象
     */
    AbstractAircraft create();
}
