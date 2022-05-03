package work.chiro.game.prop;

/**
 * @author Chiro
 */
public interface PropApplier {
    /**
     * 处理飞机碰到道具的时候的反应
     *
     * @return this
     */
    AbstractProp update();
}
