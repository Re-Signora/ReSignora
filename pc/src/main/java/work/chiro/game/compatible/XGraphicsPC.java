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
import work.chiro.game.utils.Utils;

public abstract class XGraphicsPC implements XGraphics {
    double alpha = 1.0;
    double rotation = 0.0;
    int color = 0x0;

    @Override
    public XImage<?> drawImage(XImage<?> image, double x, double y) {
        AffineTransform af = AffineTransform
                .getTranslateInstance(x * GamePanel.getScale(), y * GamePanel.getScale());
        af.scale(GamePanel.getScale(), GamePanel.getScale());
        af.rotate(rotation, image.getWidth() * GamePanel.getScale() / 2, image.getHeight() * GamePanel.getScale() / 2);
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
        if (image.getWidth() != (int) w || image.getHeight() != (int) h) {
            Utils.getLogger().warn("flush image cache!");
            BufferedImage raw = (BufferedImage) image.getImage();
            Image resizedImage = raw.getScaledInstance((int) w, (int) h, Image.SCALE_DEFAULT);
            VolatileImage bufferedImage = getXGraphicsConfiguration().createCompatibleVolatileImage((int) w, (int) h);
            Graphics2D g = bufferedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(resizedImage, 0, 0, (int) w, (int) h, null);
            g.dispose();
            return drawImage(new XImage<>() {
                @Override
                public int getWidth() {
                    return bufferedImage.getWidth();
                }

                @Override
                public int getHeight() {
                    return bufferedImage.getHeight();
                }

                @Override
                public VolatileImage getImage() {
                    return bufferedImage;
                }
            }, x, y);
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
