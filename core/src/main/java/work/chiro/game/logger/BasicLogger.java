package work.chiro.game.logger;

import work.chiro.game.config.RunningConfig;

public class BasicLogger implements AbstractLogger {
    @Override
    public void log(String message, Level level) {
        if (level == Level.DEBUG && !RunningConfig.debug) return;
        System.out.println(message);
    }

    @Override
    public void debug(String message) {
        log("[DEBUG] " + message, Level.DEBUG);
    }

    @Override
    public void info(String message) {
        log("[INFO] " + message, Level.INFO);
    }

    @Override
    public void warn(String message) {
        log("[WARN] " + message, Level.WARN);
    }

    @Override
    public void error(String message) {
        log("[ERROR] " + message, Level.ERROR);
    }

    @Override
    public void fatal(String message) {
        log("[FATAL] " + message, Level.FATAL);
    }
}
