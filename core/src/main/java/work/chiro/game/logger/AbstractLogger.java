package work.chiro.game.logger;

public interface AbstractLogger {
    void log(String message, Level level);

    void debug(String message);

    void info(String message);

    void warn(String message);

    void error(String message);

    void fatal(String message);
}
