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
public abstract class AbstractBackground extends AbstractObject<AnimateContainer> {
    protected void init() {
        initImageFilename = getInitImageFilename();
        Utils.getLogger().debug("initImageFilename: {}", initImageFilename);

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

    private static double getScaledHeight(double width, double windowWidth, double height) {
        assert width != 0;
        return windowWidth * height / width;
    }

    @Override
    public void draw(XGraphics g) {
        XImage<?> newImage = g.drawImage(getImage(), getLocationX(), getLocationY(), getWidth(), getHeight() + 2);
        if (!cachedImage.containsValue(newImage)) {
            cachedImage.put(getImage(true), newImage);
        }
        g.drawImage(newImage, getLocationX(), getLocationY() - 2 * getHeight(), getWidth(), getHeight() + 2);
        g.drawImage(newImage, getLocationX(), getLocationY() - getHeight(), getWidth(), getHeight() + 2);
        // assert getHeight() != 0;
        for (int i = 0; i <= RunningConfig.windowHeight / getHeight(); i++) {
            g.drawImage(newImage, getLocationX(), getLocationY() + getHeight() * (i + 1), getWidth(), getHeight() + 2);
        }
    }

    @Override
    public XImage<?> getImage() {
        if (cachedImage.containsKey(getImage(true))) {
            return cachedImage.get(getImage(true));
        } else {
            return super.getImage();
        }
    }

    @Override
    public boolean isValid() {
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
}
