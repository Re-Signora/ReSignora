package edu.hitsz.background;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.animate.AnimateContainerFactory;
import edu.hitsz.vector.Vec2;

/**
 * @author Chiro
 */
public abstract class BasicBackground extends AbstractBackground {
    final private String initImageFilename;

    public BasicBackground() {
        super();
        initImageFilename = null;
    }

    public BasicBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
        initImageFilename = getInitImageFilename();
    }

    /**
     * 获取初始化的文件名
     *
     * @return 文件名
     */
    abstract String getInitImageFilename();

    @Override
    public String getImageFilename() {
        return initImageFilename;
    }
}