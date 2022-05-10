package work.chiro.game.animate;

/**
 * @author Chiro
 */
public interface AnimateCallback {
    /**
     * 当所有动画执行完成后执行的钩子，用于更新动画等
     *
     * @param animateContainer 当前动画容器
     * @return 是否结束所有动画
     */
    boolean onFinish(AnimateContainer animateContainer);
}
