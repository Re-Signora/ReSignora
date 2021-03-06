package work.chiro.game.compatible;

import android.graphics.Bitmap;

import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.x.compatible.XImageFactoryInterface;

public class XImageFactoryAndroid implements XImageFactoryInterface<Bitmap> {
    private final String name;

    public XImageFactoryAndroid(String name) {
        this.name = name;
    }

    @Override
    public XImage<Bitmap> create(Bitmap image) {
        return new XImage<>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public int getWidth() {
                return image.getWidth();
            }

            @Override
            public int getHeight() {
                return image.getHeight();
            }

            @Override
            public Bitmap getImage() {
                return image;
            }

            @Override
            public int getPixel(Vec2 pos) throws ArrayIndexOutOfBoundsException {
                // return image.getColor((int) pos.getX(), (int) pos.getY()).toArgb();
                try {
                    return image.getPixel((int) pos.getX(), (int) pos.getY());
                } catch (IllegalArgumentException e) {
                    throw new ArrayIndexOutOfBoundsException();
                }
            }
        };
    }
}
