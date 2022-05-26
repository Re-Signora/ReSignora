package work.chiro.game.animate.callback;

import work.chiro.game.animate.AbstractAnimate;
import work.chiro.game.vector.VectorFactory;
import work.chiro.game.vector.VectorType;

/**
 * @author Chiro
 */
public interface AnimateCallback<T extends VectorType & VectorFactory<T>> {
    /**
     * 动画结束时调用
     * @param animate 当前动画
     */
    void onFinish(AbstractAnimate<T> animate);
}
