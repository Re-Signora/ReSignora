package edu.hitsz.background;

import edu.hitsz.animate.AnimateContainer;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.vector.Vec2;

import java.awt.*;

/**
 * 背景基类
 *
 * @author Chiro
 */
public abstract class AbstractBackground extends AbstractFlyingObject {
    public AbstractBackground(Vec2 posInit, AnimateContainer animateContainer) {
        super(posInit, animateContainer);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getImage(), (int) getLocationX(), (int) getLocationY(), null);
        g.drawImage(getImage(), (int) getLocationX(), (int) getLocationY() - Main.WINDOW_HEIGHT, null);
    }

    @Override
    public boolean notValid() {
        return false;
    }
}
