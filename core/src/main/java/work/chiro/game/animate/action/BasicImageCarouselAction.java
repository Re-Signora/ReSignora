package work.chiro.game.animate.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import work.chiro.game.config.Constants;
import work.chiro.game.objects.thing.character.AbstractThing;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.XImage;

public class BasicImageCarouselAction extends AbstractAction {
    final private String labelName;
    /**
     * 读取图像文件名前缀，包括分类文件夹等
     */
    final private String prefix;
    final private List<XImage<?>> availableImages = new ArrayList<>();

    public BasicImageCarouselAction(AbstractThing<?, AbstractAction> thing, String prefix, String labelName) {
        super(thing);
        this.labelName = labelName;
        this.prefix = prefix;
    }

    public BasicImageCarouselAction(AbstractThing<?, AbstractAction> thing, String prefix) {
        this(thing, prefix, thing.getLabelName());
    }

    public String getLabelName() {
        return labelName;
    }

    public String getImageFullPath(int index) {
        return prefix + (prefix.endsWith("/") ? "" : "/") + labelName + "-" + index + ".png";
    }

    public void loadAllAvailableImageFiles() {
        for (int i = 0; i < Constants.ACTION_MAX_IMAGE_INDEX; i++) {
            try {
                XImage<?> im = Utils.getCachedImage(getImageFullPath(i));
                availableImages.add(im);
            } catch (IOException e) {
                break;
            }
        }
    }

    public List<XImage<?>> getAvailableImages() {
        return availableImages;
    }
}
