package work.chiro.game.animate;

/**
 * 所有的动画类型
 * @author Chiro
 */
public enum AnimateType {
    // 空动画，占位
    Empty,
    // 延迟动画，仅延时，啥也不做
    Delay,
    // 线性
    Linear,
    // 非线性
    NonLinear,
    // 平滑过渡到
    SmoothTo
}
