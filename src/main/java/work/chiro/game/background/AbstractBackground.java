package work.chiro.game.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.application.Main;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.vector.Vec2;

import java.awt.*;

/**
 * 背景基类
 *
 * @author Chiro
 */
public abstract class AbstractBackground extends AbstractFlyingObject {
    public AbstractBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
        initImageFilename = getInitImageFilename();
    }

    public AbstractBackground() {
        super(new Vec2());
        initImageFilename = null;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getImage(), (int) getLocationX(), (int) getLocationY(), null);
        g.drawImage(getImage(), (int) getLocationX(), (int) getLocationY() - Main.WINDOW_HEIGHT, null);
    }

    @Override
    public boolean notValid() {
        return false;
    }

    /**
     * 生成新的对象
     *
     * @param posInit          初始位置
     * @param animateContainer 动画容器
     * @return 生成新对象
     */
    abstract AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer);

    @Override
    protected Boolean keepImage() {
        return false;
    }

    final private String initImageFilename;

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
