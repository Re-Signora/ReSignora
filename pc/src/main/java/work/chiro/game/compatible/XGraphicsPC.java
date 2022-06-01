package work.chiro.game.compatible;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.Objects;

import work.chiro.game.application.GamePanel;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.config.RunningConfigPC;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.compatible.XFont;
import work.chiro.game.x.compatible.XGraphics;
import work.chiro.game.x.compatible.XImage;
import work.chiro.game.x.ui.view.XView;

public abstract class XGraphicsPC extends XGraphics {
    private void setRenderArgs() {
        getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        getGraphics().setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        getGraphics().setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        getGraphics().setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    }

    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y) {
        setRenderArgs();
        double scale = image.isScaled() ? 1.0 : GamePanel.getScale();
        AffineTransform af = AffineTransform
                .getTranslateInstance(x * GamePanel.getScale(), y * GamePanel.getScale());
        af.scale(scale, scale);
        af.rotate(rotation, image.getWidth() * scale / 2, image.getHeight() * scale / 2);
        Graphics2D graphics2D = getGraphics();
        setAlpha(alpha);

        graphics2D.drawImage((Image) image.getImage(), af, null);
        return image;
    }

    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h) {
        if (x + w < 0 || y + h < 0 || x > RunningConfig.windowWidth || y > RunningConfig.windowHeight) {
            return image;
        }
        if (image == null) {
            return null;
        }
        Utils.CacheImageInfo cacheInfo = new Utils.CacheImageInfo((int) w, (int) h, image.getName());
        XImage<?> imageFromCache = Utils.getCachedImageFromCache(cacheInfo);
        if (((image.getWidth() != (int) w) || (image.getHeight() != (int) h)) &&
                (!image.isScaled() || (image.isScaled() && GamePanel.getJustResized())) &&
                imageFromCache == null) {
            Utils.getLogger().warn("flush image cache! im: {}", image);
            Image raw = (Image) image.getImage();
            Image resizedImage = raw.getScaledInstance((int) w, (int) h, Image.SCALE_DEFAULT);
            Graphics2D g;
            Image bufferedImage;
            if (RunningConfigPC.enableHardwareSpeedup) {
                // 硬件加速
                try {
                    bufferedImage = getXGraphicsConfiguration().createCompatibleVolatileImage((int) (w * GamePanel.getScale()), (int) (h * GamePanel.getScale()), new ImageCapabilities(true), Transparency.BITMASK);
                } catch (AWTException e) {
                    e.printStackTrace();
                    return image;
                }
                g = ((VolatileImage) bufferedImage).createGraphics();
            } else {
                bufferedImage = getXGraphicsConfiguration().createCompatibleImage((int) (w * GamePanel.getScale()), (int) (h * GamePanel.getScale()), Transparency.BITMASK);
                g = ((BufferedImage) bufferedImage).createGraphics();
            }
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(resizedImage, 0, 0, (int) (w * GamePanel.getScale()), (int) (h * GamePanel.getScale()), null);
            g.dispose();
            if (RunningConfigPC.enableHardwareSpeedup) {
                assert bufferedImage instanceof VolatileImage;
                VolatileImage im = (VolatileImage) bufferedImage;
                XImage<?> xImage = new XImage<>() {
                    @Override
                    public String getName() {
                        return image.getName();
                    }

                    @Override
                    public int getWidth() {
                        return im.getWidth();
                    }

                    @Override
                    public int getHeight() {
                        return im.getHeight();
                    }

                    @Override
                    public VolatileImage getImage() {
                        return im;
                    }

                    @Override
                    public boolean isScaled() {
                        return true;
                    }

                    @Override
                    public int getPixel(Vec2 pos) throws ArrayIndexOutOfBoundsException {
                        return im.getSnapshot().getRGB((int) pos.getX(), (int) pos.getY());
                    }
                };
                Utils.putCachedImageToCache(cacheInfo, xImage);
                return drawImage(xImage, x, y);
            } else {
                assert bufferedImage instanceof BufferedImage;
                XImage<BufferedImage> xImage = new XImageFactoryPC(image.getName()).createScaled((BufferedImage) bufferedImage);
                Utils.putCachedImageToCache(cacheInfo, xImage);
                return drawImage(xImage, x, y);
            }
        } else {
            return drawImage(Objects.requireNonNullElse(imageFromCache, image), x, y);
        }
    }

    @Override
    public XGraphics setAlpha(double alpha) {
        this.alpha = alpha;
        getGraphics().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha));
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
        getGraphics().setColor(new Color(color));
        return this;
    }

    @Override
    public XGraphics fillRect(double x, double y, double width, double height) {
        getGraphics().setColor(new Color(color));
        setRenderArgs();
        getGraphics().fillRect((int) (x * GamePanel.getScale()), (int) (y * GamePanel.getScale()), (int) (width * GamePanel.getScale()), (int) (height * GamePanel.getScale()));
        return this;
    }

    @Override
    public XGraphics ellipse(double x, double y, double r1, double r2) {
        getGraphics().setColor(new Color(color));
        setRenderArgs();
        getGraphics().drawOval((int) ((x - r1) * GamePanel.getScale()), (int) ((y - r2) * GamePanel.getScale()), (int) (r1 * 2 * GamePanel.getScale()), (int) (r2 * 2 * GamePanel.getScale()));
        return this;
    }

    @Override
    public XGraphics circle(double x, double y, double r) {
        return ellipse(x, y, r, r);
    }

    @Override
    public XGraphics drawString(String text, double x, double y) {
        getGraphics().setColor(new Color(color));
        setRenderArgs();
        getGraphics().drawString(text, (int) (x * GamePanel.getScale()), (int) ((y + getFontSize()) * GamePanel.getScale()));
        return this;
    }

    abstract protected Graphics2D getGraphics();

    abstract protected GraphicsConfiguration getXGraphicsConfiguration();

    @Override
    public XGraphics setFont(XFont<?> font) {
        super.setFont(font);
        getGraphics().setFont((Font) font.getFont());
        return this;
    }

    @Override
    public XGraphics drawUIString(XView view, String text) {
        if (view == null || text == null || text.length() == 0) return this;
        Font f = (Font) getFont().getFont();
        GlyphVector v = f.createGlyphVector(getGraphics().getFontMetrics(f).getFontRenderContext(), text);
        Shape shape = v.getOutline();

        Rectangle bounds = shape.getBounds();

        Vec2 translate = view.getPosition().fromVector(view.getPosition().times(GamePanel.getScale())
                .plus(new Vec2(view.getWidth(), view.getHeight()).times(GamePanel.getScale() / 2))
                .minus(new Vec2(bounds.getWidth(), bounds.getHeight()).divide(2))
                .minus(new Vec2(bounds.getX(), bounds.getY()))
        );
        getGraphics().translate(translate.getX(), translate.getY());
        setRenderArgs();
        getGraphics().setColor(Color.white);
        getGraphics().setStroke(new BasicStroke((float) (3.4 * GamePanel.getScale())));
        getGraphics().draw(shape);
        getGraphics().setColor(Color.black);
        getGraphics().fill(shape);
        getGraphics().translate(-translate.getX(), -translate.getY());

        return this;
    }

    @Override
    public double getFontSize() {
        return super.getFontSize() * GamePanel.getScale();
    }
}
