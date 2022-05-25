package work.chiro.game.animate.callback;

import work.chiro.game.animate.AbstractAnimate;

/**
 * @author Chiro
 */
public interface AnimateCallback {
    /**
     * 动画结束时调用
     * @param animate 当前动画
     */
    void onFinish(AbstractAnimate<?> animate);
}
