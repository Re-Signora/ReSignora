package work.chiro.game.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.compatible.XGraphics;
import work.chiro.game.compatible.XImage;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.vector.Vec2;

/**
 * 背景基类
 *
 * @author Chiro
 */
public abstract class AbstractBackground extends AbstractFlyingObject {
    private void init() {
        initImageFilename = getInitImageFilename();

        setSize(new Vec2(RunningConfig.windowWidth,
                getScaledHeight(getImage(true).getWidth(), RunningConfig.windowWidth, getImage(true).getHeight())
        ));
    }

    public AbstractBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(null, posInit, animateContainer, new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight));
        init();
    }

    public AbstractBackground() {
        super(null, new Vec2());
        init();
    }

    private static double getScaledHeight(double width, double windowWidth, double height) {
        return windowWidth * height / width;
    }

    @Override
    public void draw(XGraphics g) {
        XImage<?> newImage = g.drawImage(getImage(), getLocationX(), getLocationY(), getWidth(), getHeight());
        if (cachedImage != newImage) {
            cachedImage = newImage;
        }
        g.drawImage(cachedImage, getLocationX(), getLocationY() - getHeight(), getWidth(), getHeight());
        for (int i = 0; i <= RunningConfig.windowHeight / getHeight(); i++) {
            g.drawImage(cachedImage, getLocationX(), getLocationY() + getHeight() * (i + 1), getWidth(), getHeight());
        }
    }

    @Override
    public XImage<?> getImage() {
        if (cachedImage != null) {
            return cachedImage;
        } else {
            return super.getImage();
        }
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

    private String initImageFilename;

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

    // public XImage<?> getRawImage() throws IOException {
    //     return Utils.getCachedImage(getImageFilename());
    // }
}
