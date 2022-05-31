package work.chiro.game.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import work.chiro.game.compatible.ObjectControllerAndroidImpl;
import work.chiro.game.compatible.HistoryImplAndroid;
import work.chiro.game.compatible.ResourceProviderAndroid;
import work.chiro.game.compatible.XGraphicsAndroid;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.objects.aircraft.HeroAircraftFactory;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.storage.history.HistoryObjectFactory;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.callback.BasicCallback;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.x.activity.XActivity;
import work.chiro.game.x.compatible.ResourceProvider;

public class GameActivity extends AppCompatActivity {
    private SurfaceHolder surfaceHolder;
    private static Game game = null;
    SurfaceView surfaceView = null;
    private final ObjectControllerAndroidImpl heroControllerAndroid = new ObjectControllerAndroidImpl();

    public static Game getGame() {
        return game;
    }

    private void draw() {
        // Canvas canvas = surfaceHolder.lockCanvas();
        // 使用硬件加速
        Canvas canvas = surfaceHolder.lockHardwareCanvas();
        // 储存当前 canvas 设置
        canvas.save();
        // 设置缩放和偏移
        if (RunningConfig.allowResize) {
            canvas.scale(1.0f, 1.0f);
        } else {
            int windowWidth = surfaceView.getMeasuredWidth();
            int windowHeight = surfaceView.getMeasuredHeight();
            canvas.translate((windowWidth * 1.0f - RunningConfig.windowWidth * XGraphicsAndroid.getCanvasScale()) / 2,
                    (windowHeight * 1.0f - RunningConfig.windowHeight * XGraphicsAndroid.getCanvasScale()) / 2);
            canvas.scale(XGraphicsAndroid.getCanvasScale(), XGraphicsAndroid.getCanvasScale());
        }
        Paint paint = new Paint();
        XGraphicsAndroid xGraphics = new XGraphicsAndroid() {
            @Override
            protected Canvas getCanvas() {
                return canvas;
            }

            @Override
            public Paint getPaint() {
                return paint;
            }
        };
        canvas.drawColor(Color.BLACK);

        xGraphics.paintInOrdered(game);

        // paintInfo(xGraphics);
        xGraphics.paintInfo(game);
        // 恢复 canvas 设置之后绘制遮罩
        // canvas.restore();

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置内部模式
        RunningConfig.modePC = false;

        RunningConfig.scaleBackground = false;

        ResourceProvider.setInstance(new ResourceProviderAndroid() {
            @Override
            protected Context getContext() {
                return GameActivity.this;
            }
        });
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        surfaceView = findViewById(R.id.gameSurfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceView.post(this::resetGame);

        game = new Game(heroControllerAndroid);

        game.setOnPaint(() -> {
            if (surfaceHolder == null) {
                return;
            }
            // noinspection SynchronizeOnNonFinalField
            synchronized (surfaceHolder) {
                draw();
            }
        });
        game.setOnFinish(() -> {
            Utils.getLogger().info("FINISH!!!");
            surfaceView.post(() -> {
                EditText editName = new EditText(this);
                EditText editMessage = new EditText(this);
                editName.setText(R.string.dialog_input_name_default);
                editMessage.setText(R.string.dialog_input_message_default);
                BasicCallback goHistory = () -> {
                    startActivity(new Intent(GameActivity.this, HistoryActivity.class));
                    finish();
                };
                if (RunningConfig.score > 0) {
                    new AlertDialog.Builder(GameActivity.this, R.style.AlertDialogCustom).setTitle(R.string.dialog_input_name_title)
                            .setView(editName)
                            .setCancelable(false)
                            .setNegativeButton(R.string.button_cancel, (d, w) -> goHistory.run())
                            .setPositiveButton(R.string.button_ok, (dialogName, witch) -> surfaceView.post(() -> {
                                String name = editName.getText().toString();
                                new AlertDialog.Builder(GameActivity.this, R.style.AlertDialogCustom).setTitle(R.string.dialog_input_message_title)
                                        .setView(editMessage)
                                        .setCancelable(false)
                                        .setNegativeButton(R.string.button_cancel, (d, w) -> goHistory.run())
                                        .setPositiveButton(R.string.button_ok, (dialogMessage, witch2) -> {
                                            String message = editMessage.getText().toString();
                                            HistoryImplAndroid.getInstance(GameActivity.this).addOne(
                                                    new HistoryObjectFactory(
                                                            name,
                                                            RunningConfig.score,
                                                            message,
                                                            RunningConfig.difficulty)
                                                            .create()
                                            );
                                            HistoryImplAndroid.getInstance(GameActivity.this).display();
                                            goHistory.run();
                                        }).show();
                            }))
                            .show();
                }
            });
        });
        game.setOnFrame(heroControllerAndroid::onFrame);
        surfaceView.setOnTouchListener((v, event) -> {
            heroControllerAndroid.onTouchEvent(event);
            v.performClick();
            return true;
        });
    }

    private void resetGame() {
        if (RunningConfig.windowWidth > RunningConfig.windowHeight) {
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        int windowWidth = surfaceView.getMeasuredWidth();
        int windowHeight = surfaceView.getMeasuredHeight();

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

        game.resetStates();
    }

    @Override
    protected void onStart() {
        MyThreadFactory.getInstance().newThread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resetGame();
            game.action();
            HistoryImplAndroid.getInstance(this);
        }).start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        resetGame();
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 隐藏导航条 | 设置页面全屏显示
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
        // 不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 显示区域扩展到刘海
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onBackPressed() {
        if (getGame().getActivityManager().canExit()) {
            super.onBackPressed();
        } else {
            XActivity top = getGame().getActivityManager().getTop();
            if (top != null) {
               getGame().getActivityManager().finishActivity(top);
            }
        }
    }
}