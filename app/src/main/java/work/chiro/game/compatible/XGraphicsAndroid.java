package work.chiro.game.compatible;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;

public abstract class XGraphicsAndroid extends XGraphics {
    static private float canvasScale = 1.0f;
    static private final int fontSize = 20;
    private double alpha = 1.0;
    private double rotation = 0.0;
    private int color = 0x0;

    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y) {
        Paint paint = new Paint();
        paint.setAlpha((int) (alpha * 255));
        if (alpha == 0) {
            getCanvas().drawBitmap((Bitmap) image.getImage(), (float) x, (float) y, paint);
        } else {
            Matrix matrix = new Matrix();
            matrix.postTranslate(-(float) image.getWidth() / 2, -(float) image.getHeight() / 2);
            matrix.postRotate((float) (rotation * 180 / Math.PI));
            matrix.postTranslate((float) (image.getWidth() / 2 + x), (float) (image.getHeight() / 2 + y));
            getCanvas().drawBitmap((Bitmap) image.getImage(), matrix, paint);
        }
        return image;
    }

    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h) {
        if (image.getWidth() != (int) w || image.getHeight() != (int) h) {
            Utils.getLogger().warn("update cache image");
            Matrix matrix = new Matrix();
            matrix.postScale((float) (w / image.getWidth()), (float) (h / image.getHeight()));
            Bitmap scaledBitmap = Bitmap.createBitmap((Bitmap) image.getImage(), 0, 0, image.getWidth(), image.getHeight(), matrix, true);
            return drawImage(new XImageFactoryAndroid().create(scaledBitmap), x, y);
        } else {
            return drawImage(image, x, y);
        }
    }

    @Override
    public XGraphics setAlpha(double alpha) {
        this.alpha = alpha;
        return this;
    }

    @Override
    public XGraphics setRotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public XGraphics setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public XGraphics fillRect(double x, double y, double width, double height) {
        Paint paint = new Paint();
        paint.setColor(color);
        getCanvas().drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), paint);
        return this;
    }

    @Override
    public XGraphics drawString(String text, double x, double y) {
        Paint paint = getPaint();
        paint.setColor(color);
        paint.setTextSize(fontSize * 3.0f / canvasScale);
        getCanvas().drawText(text, (int) x, (int) y, paint);
        return this;
    }

    abstract protected Canvas getCanvas();

    public abstract Paint getPaint();

    public static float getCanvasScale() {
        return canvasScale;
    }

    public static void setCanvasScale(float canvasScale) {
        XGraphicsAndroid.canvasScale = canvasScale;
    }

    public static int getFontSize() {
        return fontSize;
    }
}
