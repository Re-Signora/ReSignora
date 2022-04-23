package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.HeroAircraftFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroController {

    public HeroController(Game game) {

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getX();
                int y = e.getY();
                if (x < 0) {
                    x = 0;
                }
                if (x > Main.WINDOW_WIDTH) {
                    x = Main.WINDOW_WIDTH;
                }
                if (y < 0) {
                    y = 0;
                }
                if (y > Main.WINDOW_HEIGHT) {
                    y = Main.WINDOW_HEIGHT;
                }
                HeroAircraft heroAircraft = HeroAircraftFactory.getInstance();
                if (heroAircraft != null) {
                    heroAircraft.setPosition(x, y);
                }
            }
        };

        game.addMouseListener(mouseAdapter);
        game.addMouseMotionListener(mouseAdapter);
    }


}
