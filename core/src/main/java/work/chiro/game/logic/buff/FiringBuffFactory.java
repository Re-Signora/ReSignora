package work.chiro.game.logic.buff;

/**
 * @author uruom
 * @version 1.0
 * @ClassName FiringBuffFactory
 * @date 2022/7/5 14:45
 */
public class FiringBuffFactory implements AbstractBuffFactory{

    @Override
    public String getName() {
        return "Firing";
    }

    @Override
    public AbstractBuff create() {
        return new FiringBuff();
    }
}
