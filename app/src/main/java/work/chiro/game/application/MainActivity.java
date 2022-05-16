package work.chiro.game.application;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

import work.chiro.game.compatible.ResourceProvider;
import work.chiro.game.compatible.XImage;
import work.chiro.game.config.Difficulty;
import work.chiro.game.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private SurfaceView surfaceView = null;
    private Canvas canvas;
    private SurfaceHolder holder;
    private boolean running = false;
    int x = 100;
    int y = 100;
    private Game game = null;

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

        game = new Game(Difficulty.Easy, () -> false);
        System.out.println("game = " + game);

        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                running = true;
                new Thread(() -> {
                    while (running) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (surfaceHolder) {
                            if (holder == null) continue;
                            canvas = holder.lockCanvas();
                            if (canvas == null) break;
                            Paint paint = new Paint();
                            paint.setColor(Color.BLACK);
                            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
                            paint.setColor(Color.RED);
                            canvas.drawRect(0, 0, x, y, paint);
                            holder.unlockCanvasAndPost(canvas);
                        }
                    }
                }).start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        canvas = holder.lockCanvas();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        running = false;
        x = (int) event.getX();
        y = (int) event.getY();
        return super.onTouchEvent(event);
    }
}