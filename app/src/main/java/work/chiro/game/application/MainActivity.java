package work.chiro.game.application;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

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
import work.chiro.game.config.Difficulty;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.thread.MyThreadFactory;
import work.chiro.game.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private SurfaceHolder surfaceHolder;
    private Game game = null;
    SurfaceView surfaceView = null;
    private final HeroControllerAndroidImpl heroControllerAndroid = new HeroControllerAndroidImpl();

    private abstract static class XGraphicsPart implements XGraphics {
        double alpha = 1.0;
        double rotation = 0.0;
        int color = 0x0;

        @Override
        public XImage<?> drawImage(XImage<?> image, double x, double y) {
            Paint paint = new Paint();
            getCanvas().drawBitmap((Bitmap) image.getImage(), (int) x, (int) y, paint);
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
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setTextSize(80);
            getCanvas().drawText(text, (int) x, (int) y, paint);
            return this;
        }

        abstract protected Canvas getCanvas();
    }

    private void draw() {
        List<List<? extends AbstractFlyingObject>> allObjects = game.getAllObjects();
        Canvas canvas = surfaceHolder.lockCanvas();
        XGraphics xGraphics = new XGraphicsPart() {
            @Override
            protected Canvas getCanvas() {
                return canvas;
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

    private void paintInfo(XGraphics g) {
        int d = 80;
        int x = 10;
        int y = d;
        g.setColor(0xcfcfcfcf);
        // g.setFont(myFontBase);
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

        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceView.post(this::onResize);

        RunningConfig.difficulty = Difficulty.Hard;
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

    private void onResize() {
        heroControllerAndroid.reset();

        RunningConfig.windowWidth = surfaceView.getMeasuredWidth();
        RunningConfig.windowHeight = surfaceView.getMeasuredHeight();

        HeroAircraftFactory.getInstance().setPosition(RunningConfig.windowWidth * 1.0 / 2, RunningConfig.windowHeight * 0.8);

        System.out.println("set window(" + RunningConfig.windowWidth + ", " + RunningConfig.windowHeight + "), place hero: " + HeroAircraftFactory.getInstance().getPosition());

        game.flushBackground();
    }

    @Override
    protected void onStart() {
        MyThreadFactory.getInstance().newThread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onResize();
            game.action();
        }).start();
        super.onStart();
    }
}