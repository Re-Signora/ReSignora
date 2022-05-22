package work.chiro.game.compatible;

import android.util.Log;

import work.chiro.game.logger.AbstractLogger;

public class LoggerAndroid implements AbstractLogger {
    static public final String TAG = "RE_SIGNORA";

    @Override
    public void log(String format, Object... args) {
        Log.v(TAG, String.format(format, args));
    }

    @Override
    public void debug(String format, Object... args) {
        Log.d(TAG, String.format(format, args));
    }

    @Override
    public void info(String format, Object... args) {
        Log.i(TAG, String.format(format, args));
    }

    @Override
    public void warn(String format, Object... args) {
        Log.w(TAG, String.format(format, args));
    }

    @Override
    public void error(String format, Object... args) {
        Log.e(TAG, String.format(format, args));
    }

    @Override
    public void fatal(String format, Object... args) {
        Log.wtf(TAG, String.format(format, args));
    }
}
