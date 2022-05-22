package work.chiro.game.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import work.chiro.game.application.GamePanel;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.config.RunningConfigPC;

public class UtilsPC {
    public static void refreshWindowSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        refreshWindowSize(screenSize.getWidth() * RunningConfigPC.ProportionForScreen, screenSize.getHeight() * RunningConfigPC.ProportionForScreen);
    }

    public static void refreshWindowSize(double windowWidth, double windowHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double scaleWidth = windowWidth / RunningConfig.windowWidth;
        double scaleHeight = windowHeight / RunningConfig.windowHeight;
        double scale = Math.min(scaleWidth, scaleHeight);
        Utils.getLogger().warn("set scale: {}", scale);
        GamePanel.setScale(scale);
        RunningConfigPC.displayWindowWidth = (int) (1.0 * RunningConfig.windowWidth * scale);
        RunningConfigPC.displayWindowHeight = (int) (1.0 * RunningConfig.windowHeight * scale);
    }
}
