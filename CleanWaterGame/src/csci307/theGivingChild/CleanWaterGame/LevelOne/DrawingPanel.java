package csci307.theGivingChild.CleanWaterGame.LevelOne;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {

    private CharacterThread characterThread;

    public DrawingPanel(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Do Stuff Here
        canvas.drawColor(Color.BLUE);
        characterThread.doDraw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
        characterThread = new CharacterThread(getHolder(), this);
        characterThread.setRunning(true);
        characterThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            characterThread.setRunning(false);
            characterThread.join();
        } catch (InterruptedException e) {}

    }

    @Override
    synchronized public boolean onTouchEvent(MotionEvent motionEvent) {
        characterThread.jump();
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}
