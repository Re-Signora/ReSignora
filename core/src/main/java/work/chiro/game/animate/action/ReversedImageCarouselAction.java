package work.chiro.game.animate.action;

/**
 * 到头后反向轮播
 */
public class ReversedImageCarouselAction extends BasicImageCarouselAction {
    private int direction = 1;

    public ReversedImageCarouselAction(String prefix, String labelName, double duration) {
        super(prefix, labelName, duration);
    }

    @Override
    protected int getNextImageIndex() {
        if (imageIndexNow == 0) direction = 1;
        else if (imageIndexNow == getAvailableImages().size() - 1) direction = -1;
        return (imageIndexNow + direction) % getAvailableImages().size();
    }
}
