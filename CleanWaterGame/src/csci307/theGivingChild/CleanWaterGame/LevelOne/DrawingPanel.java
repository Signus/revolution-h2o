package csci307.theGivingChild.CleanWaterGame.LevelOne;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {

    private PanelThread thread;

    public DrawingPanel(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Do Stuff Here
        thread.doDraw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
        thread = new PanelThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            thread.setRunning(false);
            thread.join();
        } catch (InterruptedException e) {}

    }
}
