package work.chiro.game.vector;

/**
 * 用于 Vector 子类产生、复制等
 * @author Chiro
 */
public interface VectorFactory<T> {
    /**
     * 复制对象
     * @return 复制的对象
     */
    T copy();

    /**
     * 从父类转子类
     * @param that 父类
     * @return 自留
     */
    T fromVector(VectorType that);

    /**
     * 无参数产生新对象
     * @return 新对象
     */
    T getNewInstance();
}
