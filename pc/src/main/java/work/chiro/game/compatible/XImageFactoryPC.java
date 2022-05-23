package work.chiro.game.compatible;

import java.awt.image.BufferedImage;

import work.chiro.game.vector.Vec2;

public class XImageFactoryPC implements XImageFactoryInterface<BufferedImage> {
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

            @Override
            public int getPixel(Vec2 pos) throws ArrayIndexOutOfBoundsException {
                return image.getRGB((int) pos.getX(), (int) pos.getY());
            }
        };
    }

    public XImage<BufferedImage> createScaled(BufferedImage image) {
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

            @Override
            public boolean isScaled() {
                return true;
            }

            @Override
            public int getPixel(Vec2 pos) throws ArrayIndexOutOfBoundsException {
                return image.getRGB((int) pos.getX(), (int) pos.getY());
            }
        };
    }

}
