package work.chiro.game.application;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import work.chiro.game.compatible.CharacterControllerAndroidImpl;
import work.chiro.game.compatible.HistoryImplAndroid;
import work.chiro.game.compatible.XGraphicsAndroid;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.aircraft.HeroAircraftFactory;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.UtilsAndroid;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.x.activity.XActivity;

public class GameActivity extends AppCompatActivity {
    private SurfaceHolder surfaceHolder;
    private Game game = null;
    GameView gameView = null;
    private final CharacterControllerAndroidImpl heroControllerAndroid = new CharacterControllerAndroidImpl();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置内部模式
        RunningConfig.modePC = false;
        RunningConfig.scaleBackground = false;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gameView = new GameView(this);
        setContentView(gameView);
        surfaceHolder = gameView.getHolder();

        gameView.post(this::resetGame);

        Game.clearInstance();
        game = Game.createInstance(heroControllerAndroid);
        game.setOnExit(this::finish);

        game.setOnPaint(() -> {
            if (surfaceHolder == null) {
                return;
            }
            gameView.draw();
        });
        // game.setOnFinish(() -> {
        //     Utils.getLogger().info("FINISH!!!");
        //     surfaceView.post(() -> {
        //         EditText editName = new EditText(this);
        //         EditText editMessage = new EditText(this);
        //         editName.setText(R.string.dialog_input_name_default);
        //         editMessage.setText(R.string.dialog_input_message_default);
        //         BasicCallback goHistory = () -> {
        //             startActivity(new Intent(GameActivity.this, HistoryActivity.class));
        //             finish();
        //         };
        //         if (RunningConfig.score > 0) {
        //             new AlertDialog.Builder(GameActivity.this, R.style.AlertDialogCustom).setTitle(R.string.dialog_input_name_title)
        //                     .setView(editName)
        //                     .setCancelable(false)
        //                     .setNegativeButton(R.string.button_cancel, (d, w) -> goHistory.run())
        //                     .setPositiveButton(R.string.button_ok, (dialogName, witch) -> surfaceView.post(() -> {
        //                         String name = editName.getText().toString();
        //                         new AlertDialog.Builder(GameActivity.this, R.style.AlertDialogCustom).setTitle(R.string.dialog_input_message_title)
        //                                 .setView(editMessage)
        //                                 .setCancelable(false)
        //                                 .setNegativeButton(R.string.button_cancel, (d, w) -> goHistory.run())
        //                                 .setPositiveButton(R.string.button_ok, (dialogMessage, witch2) -> {
        //                                     String message = editMessage.getText().toString();
        //                                     HistoryImplAndroid.getInstance(GameActivity.this).addOne(
        //                                             new HistoryObjectFactory(
        //                                                     name,
        //                                                     RunningConfig.score,
        //                                                     message,
        //                                                     RunningConfig.difficulty)
        //                                                     .create()
        //                                     );
        //                                     HistoryImplAndroid.getInstance(GameActivity.this).display();
        //                                     goHistory.run();
        //                                 }).show();
        //                     }))
        //                     .show();
        //         }
        //     });
        // });
        game.setOnFrame(heroControllerAndroid::onFrame);
        gameView.setOnTouchListener((v, event) -> {
            heroControllerAndroid.onTouchEvent(event);
            v.performClick();
            return true;
        });
        HistoryImplAndroid.getInstance(this);
    }

    private void resetScale() {
        UtilsAndroid.setScreen(this);
        int windowWidth = gameView.getMeasuredWidth();
        int windowHeight = gameView.getMeasuredHeight();

        if (RunningConfig.allowResize) {
            RunningConfig.windowWidth = windowWidth;
            RunningConfig.windowHeight = windowHeight;
            HeroAircraftFactory.getInstance().setPosition(RunningConfig.windowWidth / 2.0, RunningConfig.windowHeight - ImageManager.getInstance().HERO_IMAGE.getHeight());
            Utils.getLogger().info("set window(" + RunningConfig.windowWidth + ", " + RunningConfig.windowHeight + "), place hero: " + HeroAircraftFactory.getInstance().getPosition());
            heroControllerAndroid.setScale(1.0);
        } else {
            float scaleWidth = windowWidth * 1.0f / RunningConfig.windowWidth;
            float scaleHeight = windowHeight * 1.0f / RunningConfig.windowHeight;
            XGraphicsAndroid.setCanvasScale(Math.min(scaleWidth, scaleHeight));
            heroControllerAndroid.setScale(XGraphicsAndroid.getCanvasScale());
        }

    }

    private void resetGame() {
        resetScale();
    }

    @Override
    protected void onStart() {
        super.onStart();
        UtilsAndroid.setScreen(this);
        resetGame();
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
    protected void onStop() {
        resetGame();
        super.onStop();
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