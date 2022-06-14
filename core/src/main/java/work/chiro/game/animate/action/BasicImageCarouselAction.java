package work.chiro.game.animate.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import work.chiro.game.animate.Animate;
import work.chiro.game.config.Constants;
import work.chiro.game.objects.thing.AbstractThing;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;

/**
 * 图片轮播 Action
 */
public class BasicImageCarouselAction extends AbstractAction {
    final private String labelName;
    /**
     * 读取图像文件名前缀，包括分类文件夹等
     */
    final private String prefix;
    protected double duration;
    final private List<XImage<?>> availableImages = new ArrayList<>();
    protected int imageIndexNow = 0;

    public BasicImageCarouselAction(AbstractThing<?, AbstractAction> thing, String prefix, String labelName, double duration) {
        super(thing);
        this.labelName = labelName;
        this.prefix = prefix;
        this.duration = duration;
        loadAllAvailableImageFiles();
        if (availableImages.size() == 0)
            Utils.getLogger().fatal("no image loaded to ImageAction!");
        addAnimate(new Animate.Delay<>(new Vec2(), this.duration).setAnimateCallback(animate -> {
            animate.setTimeStart(TimeManager.getTimeMills());
            imageIndexNow = getNextImageIndex();
        }));
    }

    public BasicImageCarouselAction(String prefix, String labelName, double duration) {
        this(null, prefix, labelName, duration);
    }

    public String getLabelName() {
        return labelName;
    }

    public String getImageFullPath(int index) {
        return prefix + (prefix.endsWith("/") ? "" : "/") + getLabelName() + "-" + index + ".png";
    }

    public void loadAllAvailableImageFiles() {
        getAvailableImages().clear();
        for (int i = 0; i < Constants.ACTION_MAX_IMAGE_INDEX; i++) {
            try {
                XImage<?> im = Utils.getCachedImageFromResource(getImageFullPath(i));
                getAvailableImages().add(im);
            } catch (IOException e) {
                break;
            }
        }
    }

    public List<XImage<?>> getAvailableImages() {
        return availableImages;
    }

    /**
     * 获取下一个 ImageIndex，默认为按顺序从头开始
     *
     * @return next imageIndex
     */
    protected int getNextImageIndex() {
        return (imageIndexNow + 1) % getAvailableImages().size();
    }

    public XImage<?> getImageNow() {
        try {
            return getAvailableImages().get(imageIndexNow);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Boolean updateAll(double timeNow) {
        return super.updateAll(timeNow);
    }

    @Override
    public void preLoadResources(XGraphics g) {
        super.preLoadResources(g);
        getAvailableImages().forEach(g::resizeImage);
    }

    public void preLoadResources(XGraphics g, Vec2 size) {
        getAvailableImages().forEach(image -> g.resizeImage(image, size.getX(), size.getY()));
    }

    public BasicImageCarouselAction setImageIndexNow(int imageIndexNow) {
        this.imageIndexNow = imageIndexNow;
        return this;
    }

    public int getImageIndexNow() {
        return imageIndexNow;
    }
}
