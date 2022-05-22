package work.chiro.game.compatible;

import android.graphics.Bitmap;

public class XImageFactoryAndroid implements XImageFactoryInterface<Bitmap> {
    @Override
    public XImage<Bitmap> create(Bitmap image) {
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
            public Bitmap getImage() {
                return image;
            }
        };
    }
}
