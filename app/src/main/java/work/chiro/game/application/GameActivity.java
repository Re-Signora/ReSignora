package work.chiro.game.application;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.IOException;
import java.util.List;

import work.chiro.game.aircraft.BossEnemy;
import work.chiro.game.aircraft.BossEnemyFactory;
import work.chiro.game.aircraft.HeroAircraftFactory;
import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.compatible.ResourceProvider;
import work.chiro.game.compatible.XGraphics;
import work.chiro.game.compatible.XImage;
import work.chiro.game.compatible.XImageFactory;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.resource.ImageManager;
import work.chiro.game.thread.MyThreadFactory;
import work.chiro.game.utils.Utils;

public class GameActivity extends AppCompatActivity {
    private SurfaceHolder surfaceHolder;
    private Game game = null;
    SurfaceView surfaceView = null;
    private final HeroControllerAndroidImpl heroControllerAndroid = new HeroControllerAndroidImpl();
    private Typeface font;

    private abstract static class XGraphicsPart implements XGraphics {
        double alpha = 1.0;
        double rotation = 0.0;
        int color = 0x0;

        @Override
        public XImage<?> drawImage(XImage<?> image, double x, double y) {
            Paint paint = new Paint();
            paint.setAlpha((int) (alpha * 255));
            if (alpha == 0) {
                getCanvas().drawBitmap((Bitmap) image.getImage(), (float) x, (float) y, paint);
            } else {
                Matrix matrix = new Matrix();
                matrix.postTranslate(-(float) image.getWidth() / 2, -(float) image.getHeight() / 2);
                matrix.postRotate((float) (rotation * 180 / Math.PI));
                matrix.postTranslate((float) (image.getWidth() / 2 + x), (float) (image.getHeight() / 2 + y));
                getCanvas().drawBitmap((Bitmap) image.getImage(), matrix, paint);
            }
            return image;
        }

        @Override
        public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h) {
            if (image.getWidth() != (int) w || image.getHeight() != (int) h) {
                System.out.println("WARN: update cache image");
                Matrix matrix = new Matrix();
                matrix.postScale((float) (w / image.getWidth()), (float) (h / image.getHeight()));
                Bitmap scaledBitmap = Bitmap.createBitmap((Bitmap) image.getImage(), 0, 0, image.getWidth(), image.getHeight(), matrix, true);
                return drawImage(new XImageFactory().create(scaledBitmap), x, y);
            } else {
                return drawImage(image, x, y);
            }
        }

        @Override
        public XGraphics setAlpha(double alpha) {
            this.alpha = alpha;
            return this;
        }

        @Override
        public XGraphics setRotation(double rotation) {
            this.rotation = rotation;
            return this;
        }

        @Override
        public XGraphics setColor(int color) {
            this.color = color;
            return this;
        }

        @Override
        public XGraphics fillRect(double x, double y, double width, double height) {
            Paint paint = new Paint();
            paint.setColor(color);
            getCanvas().drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), paint);
            return this;
        }

        @Override
        public XGraphics drawString(String text, double x, double y) {
            Paint paint = getPaint();
            paint.setColor(color);
            paint.setTextSize(60);
            getCanvas().drawText(text, (int) x, (int) y, paint);
            return this;
        }

        abstract protected Canvas getCanvas();

        abstract protected Paint getPaint();
    }

    private void draw() {
        List<List<? extends AbstractFlyingObject>> allObjects = game.getAllObjects();
        // Canvas canvas = surfaceHolder.lockCanvas();
        Canvas canvas = surfaceHolder.lockHardwareCanvas();
        Paint paint = new Paint();
        XGraphicsPart xGraphics = new XGraphicsPart() {
            @Override
            protected Canvas getCanvas() {
                return canvas;
            }

            @Override
            protected Paint getPaint() {
                return paint;
            }
        };
        canvas.drawColor(Color.BLACK);

        // 绘制所有物体
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (allObjects) {
            allObjects.forEach(objList -> {
                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (objList) {
                    objList.forEach(obj -> obj.draw(xGraphics));
                }
            });
        }

        paintInfo(xGraphics);

        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private void paintInfo(XGraphicsPart g) {
        int d = 60;
        int x = 10;
        int y = d;
        g.setColor(0xcfcfcfcf);
        g.getPaint().setTypeface(font);
        g.drawString("SCORE:" + (int) (RunningConfig.score), x, y);
        y = y + d;
        g.drawString("LIFE:" + (int) (HeroAircraftFactory.getInstance().getHp()), x, y);
        y = y + d;
        BossEnemy boss = BossEnemyFactory.getInstance();
        if (boss == null) {
            g.drawString("Before Boss:" + (int) (game.getNextBossScore() - RunningConfig.score), x, y);
        } else {
            g.drawString("BOSS LIFE:" + (int) (boss.getHp()), x, y);
        }
        y = y + d;
        g.drawString("FPS:" + game.getTimerController().getFps(), x, y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourceProvider.setInstance(new ResourceProvider() {
            @Override
            public XImage<?> getImageFromResource(String path) throws IOException {
                Bitmap bitmap = BitmapFactory.decodeStream(Utils.class.getResourceAsStream("/images/" + path));
                if (bitmap == null) {
                    throw new IOException("file: " + path + " not found!");
                }
                return new XImageFactory().create(bitmap);
            }
        });

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
        font = ResourcesCompat.getFont(this, R.font.genshin);

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
            System.out.println("FINISH!!!");
            game.resetStates();
            game.action();
        });
        surfaceView.setOnTouchListener((v, event) -> {
            heroControllerAndroid.onTouchEvent(event);
            v.performClick();
            return true;
        });
    }

    private void resetGame() {
        heroControllerAndroid.reset();

        RunningConfig.windowWidth = surfaceView.getMeasuredWidth();
        RunningConfig.windowHeight = surfaceView.getMeasuredHeight();

        HeroAircraftFactory.getInstance().setPosition(RunningConfig.windowWidth / 2.0, RunningConfig.windowHeight - ImageManager.getInstance().HERO_IMAGE.getHeight());

        System.out.println("set window(" + RunningConfig.windowWidth + ", " + RunningConfig.windowHeight + "), place hero: " + HeroAircraftFactory.getInstance().getPosition());

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
        }).start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        resetGame();
        super.onStop();
    }
}