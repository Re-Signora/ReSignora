package work.chiro.game.compatible;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import work.chiro.game.application.GamePanel;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.config.RunningConfigPC;
import work.chiro.game.utils.Utils;
import work.chiro.game.vector.Vec2;

public abstract class XGraphicsPC implements XGraphics {
    double alpha = 1.0;
    double rotation = 0.0;
    int color = 0x0;

    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y) {
        double scale = image.isScaled() ? 1.0 : GamePanel.getScale();
        AffineTransform af = AffineTransform
                .getTranslateInstance(x * GamePanel.getScale(), y * GamePanel.getScale());
        af.scale(scale, scale);
        af.rotate(rotation, image.getWidth() * scale / 2, image.getHeight() * scale / 2);
        Graphics2D graphics2D = getGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha));
        graphics2D.drawImage((Image) image.getImage(), af, null);
        return image;
    }

    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h) {
        if (x + w < 0 || y + h < 0 || x > RunningConfig.windowWidth || y > RunningConfig.windowHeight) {
            return image;
        }
        if (((image.getWidth() != (int) w) || (image.getHeight() != (int) h)) && (!image.isScaled() || (image.isScaled() && GamePanel.getJustResized()))) {
            Utils.getLogger().warn("flush image cache!");
            Image raw = (Image) image.getImage();
            Image resizedImage = raw.getScaledInstance((int) w, (int) h, Image.SCALE_DEFAULT);
            // 硬件加速
            Graphics2D g;
            Image bufferedImage;
            if (RunningConfigPC.enableHardwareSpeedup) {
                bufferedImage = getXGraphicsConfiguration().createCompatibleVolatileImage((int) (w * GamePanel.getScale()), (int) (h * GamePanel.getScale()));
                g = ((VolatileImage) bufferedImage).createGraphics();
            } else {
                bufferedImage = getXGraphicsConfiguration().createCompatibleImage((int) (w * GamePanel.getScale()), (int) (h * GamePanel.getScale()));
                g = ((BufferedImage) bufferedImage).createGraphics();
            }
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(resizedImage, 0, 0, (int) (w * GamePanel.getScale()), (int) (h * GamePanel.getScale()), null);
            g.dispose();
            if (RunningConfigPC.enableHardwareSpeedup) {
                assert bufferedImage instanceof VolatileImage;
                VolatileImage im = (VolatileImage) bufferedImage;
                return drawImage(new XImage<>() {
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
                    public int getPixel(Vec2 pos) {
                        return im.getSnapshot().getRGB((int) pos.getX(), (int) pos.getY());
                    }
                }, x, y);
            } else {
                assert bufferedImage instanceof BufferedImage;
                return drawImage(new XImageFactoryPC().createScaled((BufferedImage) bufferedImage), x, y);
            }
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
        getGraphics().setColor(new Color(color));
        getGraphics().fillRect((int) (x * GamePanel.getScale()), (int) (y * GamePanel.getScale()), (int) (width * GamePanel.getScale()), (int) (height * GamePanel.getScale()));
        return this;
    }

    @Override
    public XGraphics drawString(String text, double x, double y) {
        getGraphics().drawString(text, (int) (x * GamePanel.getScale()), (int) (y * GamePanel.getScale()));
        return this;
    }

    abstract protected Graphics2D getGraphics();

    abstract protected GraphicsConfiguration getXGraphicsConfiguration();
}
