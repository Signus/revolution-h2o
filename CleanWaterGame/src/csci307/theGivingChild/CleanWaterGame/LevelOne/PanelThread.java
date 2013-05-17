package csci307.theGivingChild.CleanWaterGame.LevelOne;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import csci307.theGivingChild.CleanWaterGame.LevelOne.DrawingPanel;

public class PanelThread extends Thread {

    public static final float SPEED = 2;
    public static final float RADIUS = 50;
    private SurfaceHolder surfaceHolder;
    private DrawingPanel drawingPanel;
    private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean run = false;

    private float x;
    private float y;
    private float v_x;
    private float v_y;

    public PanelThread(SurfaceHolder surfaceHolder, DrawingPanel drawingPanel) {
        this.surfaceHolder = surfaceHolder;
        this.drawingPanel = drawingPanel;
        x = 100;
        y = 100;
        v_x = (float) (-1 + (Math.random()*2));
        v_y = (float) (-1 + (Math.random()*2));
        paint.setColor(Color.RED);
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        Canvas c;
        while (run) {
            c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    doDraw(c);
                    drawingPanel.postInvalidate();
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void doDraw(Canvas canvas) {
        x += v_x * SPEED;
        y += v_y * SPEED;
        if (x - RADIUS <= 0 || x + RADIUS >= canvas.getWidth()) {
            v_x *= -1;
        }
        if (y - RADIUS <= 0 || y + RADIUS >= canvas.getHeight()) {
            v_y *= -1;
        }
        canvas.drawColor(Color.BLUE);
        canvas.drawCircle(x, y, RADIUS, paint);
    }
}
