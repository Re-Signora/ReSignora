package work.chiro.game.application;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import work.chiro.game.compatible.ResourceProviderAndroid;
import work.chiro.game.compatible.XGraphicsAndroid;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.game.Game;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.compatible.XGraphics;

public class GameView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {
    private final SurfaceHolder surfaceHolder;
    private final Context context;
    public GameView(Context context) {
        super(context);
        this.context = context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        this.setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void run() {
        ResourceProvider.setInstance(new ResourceProviderAndroid() {
            @Override
            protected Context getContext() {
                return context;
            }
            private Canvas lastCanvas = null;

            @Override
            public XGraphics getXGraphics() {
                XGraphicsAndroid g = GameView.this.getXGraphics();
                lastCanvas = g.getCanvas();
                return g;
            }

            @Override
            public void stopXGraphics() {
                super.stopXGraphics();
                surfaceHolder.unlockCanvasAndPost(lastCanvas);
                lastCanvas = null;
            }
        });
    }

    private XGraphicsAndroid getXGraphics() {
        // Utils.getLogger().warn("will lock at thread: {}", Thread.currentThread());
        // Canvas canvas = surfaceHolder.lockCanvas();
        // 使用硬件加速
        Canvas canvas = surfaceHolder.lockHardwareCanvas();
        // 储存当前 canvas 设置
        canvas.save();
        // 设置缩放和偏移
        if (RunningConfig.allowResize) {
            canvas.scale(1.0f, 1.0f);
        } else {
            int windowWidth = getMeasuredWidth();
            int windowHeight = getMeasuredHeight();
            canvas.translate((windowWidth * 1.0f - RunningConfig.windowWidth * XGraphicsAndroid.getCanvasScale()) / 2,
                    (windowHeight * 1.0f - RunningConfig.windowHeight * XGraphicsAndroid.getCanvasScale()) / 2);
            canvas.scale(XGraphicsAndroid.getCanvasScale(), XGraphicsAndroid.getCanvasScale());
        }
        Paint paint = new Paint();
        XGraphicsAndroid xGraphics = new XGraphicsAndroid() {
            private Paint p = paint;

            @Override
            public Canvas getCanvas() {
                return canvas;
            }

            @Override
            public Paint getPaint() {
                return p;
            }

            @Override
            public Paint getNewPaint() {
                p = new Paint();
                return p;
            }
        };
        canvas.drawColor(Color.BLACK);
        return xGraphics;
    }

    public void draw() {
        if (Game.getInstance() == null) return;
        synchronized (surfaceHolder) {
            XGraphicsAndroid xGraphics = getXGraphics();
            xGraphics.getCanvas().drawColor(Color.BLACK);
            xGraphics.paintInOrdered(Game.getInstance());
            xGraphics.paintInfo(Game.getInstance());
            // 恢复 canvas 设置之后绘制遮罩
            surfaceHolder.unlockCanvasAndPost(xGraphics.getCanvas());
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        Utils.getLogger().warn("gameView: onWindowVisibilityChanged");
        super.onWindowVisibilityChanged(visibility);
    }
}
