package work.chiro.game.animate.callback;

import work.chiro.game.animate.AnimateContainer;

/**
 * @author Chiro
 */
public interface AnimateContainerCallback {
    /**
     * 当所有动画执行完成后执行的钩子，用于更新动画等
     *
     * @param animateContainer 当前动画容器
     * @return 是否结束所有动画
     */
    boolean onFinish(AnimateContainer animateContainer);
}
