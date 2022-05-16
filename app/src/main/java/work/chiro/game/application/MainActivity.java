package work.chiro.game.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import work.chiro.game.basic.AbstractFlyingObject;
import work.chiro.game.compatible.ResourceProvider;
import work.chiro.game.compatible.XGraphics;
import work.chiro.game.compatible.XImage;
import work.chiro.game.config.Difficulty;
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
        public XGraphics drawImage(XImage<?> image, double x, double y) {
//            AffineTransform af = AffineTransform.getTranslateInstance(x, y);
//            af.rotate(rotation, image.getWidth() * 1.0 / 2, image.getHeight() * 1.0 / 2);
//            Graphics2D graphics2D = (Graphics2D) (getGraphics());
//            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, (float) alpha));
//            graphics2D.drawImage((Image) image.getImage(), af, null);

            getCanvas().drawBitmap((Bitmap) image.getImage(), (int) x, (int) y, getPaint());
            return this;
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
                return new XImage<Bitmap>() {
                    @Override
                    public int getWidth() {
                        return bitmap.getWidth();
                    }

                    @Override
                    public int getHeight() {
                        return bitmap.getHeight();
                    }

                    @Override
                    public Bitmap getImage() {
                        return bitmap;
                    }
                };
            }
        });

        game = new Game(Difficulty.Easy, heroControllerAndroid);
        System.out.println("game = " + game);

        game.setOnFinish(() -> System.out.println("on finish"));

        setContentView(R.layout.activity_main);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        game.setOnPaint(() -> {
            if (surfaceHolder == null) {
                return;
            }
            // noinspection SynchronizeOnNonFinalField
            synchronized (surfaceHolder) {
                System.out.println("paint()!");
                Paint paint = new Paint();
                draw(paint);
            }
        });
        game.setOnFinish(() -> System.out.println("FINISH!!!"));
        surfaceView.setOnTouchListener((v, event) -> {
            draw(new Paint());
            heroControllerAndroid.onTouchEvent(event);
            v.performClick();
            return true;
        });
        MyThreadFactory.getInstance().newThread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.action();
        }).start();
    }
}