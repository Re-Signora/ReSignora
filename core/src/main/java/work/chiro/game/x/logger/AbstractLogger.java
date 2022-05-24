package work.chiro.game.x.logger;

public interface AbstractLogger {
    void log(String format, Object... args);

    void debug(String format, Object... args);

    void info(String format, Object... args);

    void warn(String format, Object... args);

    void error(String format, Object... args);

    void fatal(String format, Object... args);
}
