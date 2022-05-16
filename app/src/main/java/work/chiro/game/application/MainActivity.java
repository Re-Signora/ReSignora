package work.chiro.game.application;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import work.chiro.game.config.Difficulty;

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