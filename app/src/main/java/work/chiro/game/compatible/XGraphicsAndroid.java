package work.chiro.game.compatible;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.Objects;

import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XFont;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.x.ui.view.XView;

public abstract class XGraphicsAndroid extends XGraphics {
    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y, boolean flipped) {
        if (image == null) return null;
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
    public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h, boolean flipped) {
        if (image == null) return null;
        Utils.CacheImageInfo info = new Utils.CacheImageInfo((int) w, (int) h, image.getName());
        XImage<?> cachedImage = Utils.getCachedImageFromCache(info);
        if ((image.getWidth() != (int) w || image.getHeight() != (int) h) && cachedImage == null) {
            return drawImage(resizeImage(image, w, h), x, y, flipped);
        } else {
            return drawImage(Objects.requireNonNullElse(cachedImage, image), x, y, flipped);
        }
    }

    @Override
    public XImage<?> resizeImage(XImage<?> image, double w, double h) {
        Utils.CacheImageInfo info = new Utils.CacheImageInfo((int) w, (int) h, image.getName());
        XImage<?> cachedImage = Utils.getCachedImageFromCache(info);
        if (cachedImage != null) return cachedImage;
        Utils.getLogger().warn("update cache image: {}", image);
        Matrix matrix = new Matrix();
        matrix.postScale((float) (w / image.getWidth()), (float) (h / image.getHeight()));
        Bitmap scaledBitmap = Bitmap.createBitmap((Bitmap) image.getImage(), 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        XImage<Bitmap> xImage = new XImageFactoryAndroid(image.getName()).create(scaledBitmap);
        Utils.putCachedImageToCache(info, xImage);
        return xImage;
    }

    @Override
    public XGraphics setAlpha(double alpha) {
        this.alpha = alpha;
        getPaint().setAlpha((int) (alpha * 255));
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
        getPaint().setColor(color);
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
        if (text == null || text.length() == 0) return this;
        setColor(color);
        getPaint().setTextSize((float) (getFontSize() / getCanvasScale()));
        getCanvas().drawText(text, (int) x, (int) y, getPaint());
        return this;
    }

    abstract public Canvas getCanvas();

    public abstract Paint getPaint();

    public abstract Paint getNewPaint();

    @Override
    public XGraphics setFont(XFont<?> font) {
        super.setFont(font);
        getPaint().setTypeface((Typeface) getFont().getFont());
        return this;
    }

    @Override
    public XGraphics ellipse(double x, double y, double r1, double r2) {
        setColor(color);
        getPaint().setStyle(Paint.Style.STROKE);
        getCanvas().drawOval((float) (x - r1), (float) (y - r2), (float) (x + r1), (float) (y + r2), getPaint());
        return this;
    }

    @Override
    public XGraphics circle(double x, double y, double r) {
        setColor(color);
        getPaint().setStyle(Paint.Style.STROKE);
        getCanvas().drawCircle((float) x, (float) y, (float) r, getPaint());
        return this;
    }

    @Override
    public XGraphics drawBoarderString(Vec2 position, Vec2 size, String text) {
        if (text == null) return this;
        if (text.length() == 0) return this;
        setAlpha(alpha);
        getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        getPaint().setFakeBoldText(true);
        getPaint().setStrokeWidth((float) (4.5 * getCanvasScale()));
        getPaint().setTextSize((float) (getFontSize() / getCanvasScale()));

        // 测量绘制大小
        Rect bounds = new Rect();
        Vec2 boundsSize = new Vec2(bounds.right - bounds.left, bounds.bottom - bounds.top);
        getPaint().getTextBounds(text, 0, text.length(), bounds);
        Vec2 translate = position.fromVector(position
                .plus((size == null ? boundsSize : size).divide(2))
                .minus(new Vec2(bounds.right - bounds.left, bounds.bottom - bounds.top).divide(2))
                .minus(new Vec2(bounds.left, bounds.top))
        );
        getCanvas().translate((float) translate.getX(), (float) translate.getY());
        // 描边
        getPaint().setColor(colorBoarder);
        getCanvas().drawText(text, 0, 0, getPaint());
        // 文字
        getPaint().setFakeBoldText(false);
        getPaint().setColor(color);
        getPaint().setStrokeWidth(0);
        getCanvas().drawText(text, 0, 0, getPaint());
        getCanvas().translate((float) -translate.getX(), (float) -translate.getY());
        return this;
    }

    @Override
    public XGraphics drawUIString(XView view, String text) {
        if (view == null || text == null) return this;
        if (text.length() == 0) return this;
        getNewPaint();
        setFont(view.getFont());
        return drawBoarderString(view.getPosition(), new Vec2(view.getWidth(), view.getHeight()), text);
    }

    @Override
    public double getFontSize() {
        return super.getFontSize();
    }

    @Override
    public void paintInfo(Game game) {
        getNewPaint();
        super.paintInfo(game);
    }
}
