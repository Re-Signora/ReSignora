package work.chiro.game.compatible;

import java.awt.image.BufferedImage;

public class XImageFactory implements XImageFactoryInterface<BufferedImage> {
    @Override
    public XImage<BufferedImage> create(BufferedImage image) {
        return new XImage<>() {
            @Override
            public int getWidth() {
                return image.getWidth();
            }

            @Override
            public int getHeight() {
                return image.getHeight();
            }

            @Override
            public BufferedImage getImage() {
                return image;
            }
        };
    }
}
