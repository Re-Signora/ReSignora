package work.chiro.game.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.vector.Vec2;

public class BasicBackground extends AbstractBackground {
    final private String backgroundName;
    public BasicBackground(String backgroundName) {
        this.backgroundName = backgroundName;
        // 重新初始化数据
        init();
    }

    @Override
    public AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer) {
        return new BasicBackground(null);
    }

    @Override
    public String getInitImageFilename() {
        return backgroundName;
    }

    @Override
    protected Boolean keepImage() {
        return true;
    }
}
