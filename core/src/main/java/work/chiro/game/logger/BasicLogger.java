package work.chiro.game.logger;

import java.io.PrintStream;

import work.chiro.game.config.RunningConfig;

public class BasicLogger implements AbstractLogger {
    private static final String DELIM_STR = "{}";
    private static final Object[] EMPTY_ARGS = new Object[0];

    // from: https://blog.csdn.net/10km/article/details/79719592
    public void log(PrintStream printStream, int stackLevel, String format, Object... args) {
        if (null == printStream || null == format) {
            return;
        }
        if (null == args) {
            args = EMPTY_ARGS;
        }
        StringBuilder buffer = new StringBuilder(format.length() + 64);
        int beginIndex = 0, endIndex, count = 0;
        while ((endIndex = format.indexOf(DELIM_STR, beginIndex)) >= 0) {
            buffer.append(format, beginIndex, endIndex);
            try {
                buffer.append(args[count++]);
            } catch (IndexOutOfBoundsException e) {
                // 数组越界时对应占位符填null
                buffer.append("null");
            }
            beginIndex = endIndex + DELIM_STR.length();
        }
        buffer.append(format.substring(beginIndex));
        Thread currentThread = Thread.currentThread();
        StackTraceElement stackTrace = currentThread.getStackTrace()[stackLevel];
        printStream.printf("[%s] (%s:%d) %s\n",
                currentThread.getName(),
                stackTrace.getFileName(),
                stackTrace.getLineNumber(),
                buffer);
    }

    public void log(String format, int stackLevel, Object... args) {
        log(System.out, stackLevel + 1, format, args);
    }

    @Override
    public void log(String format, Object... args) {
        log(System.out, 3, format, args);
    }

    @Override
    public void debug(String format, Object... args) {
        if (!RunningConfig.debug) return;
        log("[DEBUG] " + format, 3, args);
    }

    @Override
    public void info(String format, Object... args) {
        log("[INFO] " + format, 3, args);
    }

    @Override
    public void warn(String format, Object... args) {
        log("[WARN] " + format, 3, args);
    }

    @Override
    public void error(String format, Object... args) {
        log("[ERROR] " + format, 3, args);
    }

    @Override
    public void fatal(String format, Object... args) {
        log("[FATAL] " + format, 3, args);
    }
}
