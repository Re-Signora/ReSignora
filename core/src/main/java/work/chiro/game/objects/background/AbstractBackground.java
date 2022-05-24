package work.chiro.game.objects.background;

import work.chiro.game.animate.AnimateContainer;
import work.chiro.game.objects.AbstractObject;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

/**
 * 背景基类
 *
 * @author Chiro
 */
public abstract class AbstractBackground extends AbstractObject {
    protected void init() {
        initImageFilename = getInitImageFilename();
        Utils.getLogger().info("initImageFilename: {}", initImageFilename);

        if (getImage(true) != null) {
            if (RunningConfig.scaleBackground) {
                Vec2 scaledSize = new Vec2(RunningConfig.windowWidth,
                        getScaledHeight(getImage(true).getWidth(), RunningConfig.windowWidth, getImage(true).getHeight())
                );
                Utils.getLogger().info("background set size: {}", scaledSize);
                setSize(scaledSize);
            } else {
                setSize(new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight));
            }
        }
    }

    public AbstractBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer, new Vec2(RunningConfig.windowWidth, RunningConfig.windowHeight));
        init();
    }

    public AbstractBackground() {
        super(new Vec2());
        init();
    }

    private static double getScaledHeight(double width, double windowWidth, double height) {
        return windowWidth * height / width;
    }

    @Override
    public void draw(XGraphics g) {
        XImage<?> newImage = g.drawImage(getImage(), getLocationX(), getLocationY(), getWidth(), getHeight() + 2);
        if (cachedImage != newImage) {
            cachedImage = newImage;
        }
        g.drawImage(cachedImage, getLocationX(), getLocationY() - 2 * getHeight(), getWidth(), getHeight() + 2);
        g.drawImage(cachedImage, getLocationX(), getLocationY() - getHeight(), getWidth(), getHeight() + 2);
        for (int i = 0; i <= RunningConfig.windowHeight / getHeight(); i++) {
            g.drawImage(cachedImage, getLocationX(), getLocationY() + getHeight() * (i + 1), getWidth(), getHeight() + 2);
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
    public abstract AbstractBackground newInstance(Vec2 posInit, AnimateContainer animateContainer);

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
    public abstract String getInitImageFilename();

    @Override
    public String getImageFilename() {
        return initImageFilename;
    }

    // public XImage<?> getRawImage() throws IOException {
    //     return Utils.getCachedImage(getImageFilename());
    // }
}
