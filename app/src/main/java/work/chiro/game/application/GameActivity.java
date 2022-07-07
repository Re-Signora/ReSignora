package work.chiro.game.application;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import work.chiro.game.compatible.CharacterControllerAndroidImpl;
import work.chiro.game.compatible.HistoryImplAndroid;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.utils.UtilsAndroid;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.x.activity.XActivity;

public class GameActivity extends AppCompatActivity {
    private SurfaceHolder surfaceHolder;
    private Game game = null;
    GameView gameView = null;
    private final CharacterControllerAndroidImpl heroControllerAndroid = new CharacterControllerAndroidImpl();
//为什么跳到了andorid上面啊qwq
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置内部模式
        RunningConfig.modePC = false;
        // RunningConfig.enableImageCache = false;
        RunningConfig.scaleBackground = false;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameView = new GameView(this);
        setContentView(gameView);
        surfaceHolder = gameView.getHolder();

//        gameView.post(this::resetGame);

        Game.clearInstance();
        game = Game.createInstance(heroControllerAndroid);
        game.setOnExit(this::finish);

        game.setOnPaint(() -> {
            if (surfaceHolder == null) {
                return;
            }
            gameView.draw();
        });

        game.setOnFrame(heroControllerAndroid::onFrame);
        gameView.setOnTouchListener((v, event) -> {
            heroControllerAndroid.onTouchEvent(event);
            v.performClick();
            return true;
        });
        HistoryImplAndroid.getInstance(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        UtilsAndroid.setScreen(this);
//        前面的好像是自带的吧~？？？？
//        resetGame();
        MyThreadFactory.getInstance().newThread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.action();
        }, "StartGame").start();
    }


    @Override
    public void onBackPressed() {
        if (Game.getInstance().getActivityManager().canExit()) {
            super.onBackPressed();
        } else {
            XActivity top = Game.getInstance().getActivityManager().getTop();
            if (top != null) {
                Game.getInstance().getActivityManager().finishActivity(top);
            }
        }
    }
}