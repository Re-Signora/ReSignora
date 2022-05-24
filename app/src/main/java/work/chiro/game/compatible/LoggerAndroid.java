package work.chiro.game.compatible;

import android.util.Log;

import work.chiro.game.config.RunningConfig;
import work.chiro.game.x.logger.AbstractLogger;

public class LoggerAndroid implements AbstractLogger {
    static public final String TAG = "RE_SIGNORA";
    private static final String DELIM_STR = "{}";

    StringBuilder parseFormatString(String format, Object... args) {
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
        return buffer;
    }

    @Override
    public void log(String format, Object... args) {
        Log.v(TAG, parseFormatString(format, args).toString());
    }

    @Override
    public void debug(String format, Object... args) {
        if (RunningConfig.debug) {
            Log.d(TAG, parseFormatString(format, args).toString());
        }
    }

    @Override
    public void info(String format, Object... args) {
        Log.i(TAG, parseFormatString(format, args).toString());
    }

    @Override
    public void warn(String format, Object... args) {
        Log.w(TAG, parseFormatString(format, args).toString());
    }

    @Override
    public void error(String format, Object... args) {
        Log.e(TAG, parseFormatString(format, args).toString());
    }

    @Override
    public void fatal(String format, Object... args) {
        Log.wtf(TAG, parseFormatString(format, args).toString());
    }
}
