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
    private final HeroControllerAndroidImpl heroControllerAndroid = new HeroControllerAndroidImpl();

    private abstract static class XGraphicsPart implements XGraphics {
        double alpha = 1.0;
        double rotation = 0.0;
        int color = 0x0;

        @Override
        public XImage<?> drawImage(XImage<?> image, double x, double y) {
            getCanvas().drawBitmap((Bitmap) image.getImage(), (int) x, (int) y, getPaint());
            return image;
        }

        @Override
        public XImage<?> drawImage(XImage<?> image, double x, double y, double w, double h) {
            // getCanvas().drawBitmap((Bitmap) image.getImage(), new Rect(0, 0, image.getWidth(), image.getHeight()), new Rect((int) x, (int) y, (int) (w + x), (int) (h + y)), getPaint());
            if (image.getWidth() != (int) w || image.getHeight() != (int) h) {
                Matrix matrix = new Matrix();
                matrix.postScale((float) (w / image.getWidth()), (float) (h / image.getHeight()));
                Bitmap scaledBitmap = Bitmap.createBitmap((Bitmap) image.getImage(), 0, 0, image.getWidth(), image.getHeight(), matrix, true);
                getCanvas().drawBitmap((Bitmap) image.getImage(), matrix, getPaint());
                return new XImageFactory().create(scaledBitmap);
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
            getPaint().setColor(color);
            getCanvas().drawRect((int) x, (int) y, (int) width, (int) height, getPaint());
            return this;
        }

        abstract protected Paint getPaint();

        abstract protected Canvas getCanvas();
    }

    private void draw(Paint paint) {
        List<List<? extends AbstractFlyingObject>> allObjects = game.getAllObjects();
        Canvas canvas = surfaceHolder.lockCanvas();
        XGraphics xGraphics = new XGraphicsPart() {
            @Override
            protected Paint getPaint() {
                return paint;
            }

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
        surfaceHolder.unlockCanvasAndPost(canvas);
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
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceView.post(() -> {
            RunningConfig.windowWidth = surfaceView.getMeasuredWidth();
            RunningConfig.windowHeight = surfaceView.getMeasuredHeight();

            System.out.println("set window(" + RunningConfig.windowWidth + ", " + RunningConfig.windowHeight + ")");
            game.flushBackground();
        });

        game = new Game(Difficulty.Easy, heroControllerAndroid);
        System.out.println("game = " + game);

        game.setOnFinish(() -> System.out.println("on finish"));

        game.setOnPaint(() -> {
            if (surfaceHolder == null) {
                return;
            }
            // noinspection SynchronizeOnNonFinalField
            synchronized (surfaceHolder) {
                Paint paint = new Paint();
                draw(paint);
            }
        });
        game.setOnFinish(() -> System.out.println("FINISH!!!"));
        surfaceView.setOnTouchListener((v, event) -> {
            heroControllerAndroid.onTouchEvent(event);
            v.performClick();
            return true;
        });
    }

    @Override
    protected void onStart() {
        MyThreadFactory.getInstance().newThread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.action();
        }).start();
        super.onStart();
    }
}