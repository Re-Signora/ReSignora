package work.chiro.game.logic.buff;

/**
 * @author uruom
 * @version 1.0
 * @ClassName AbstractBuffFactory
 * @date 2022/7/1 11:22
 */
public interface AbstractBuffFactory {
    String getName();
    AbstractBuff create();

}
