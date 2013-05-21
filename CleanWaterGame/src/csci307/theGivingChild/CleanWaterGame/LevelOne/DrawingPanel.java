package csci307.theGivingChild.CleanWaterGame.LevelOne;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.*;

public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {

    private CharacterThread characterThread;
    private GestureDetector gestureScanner;

    public DrawingPanel(Context context) {
        super(context);
        getHolder().addCallback(this);
        gestureScanner = new GestureDetector(context, this);
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
        endThreads();
    }

    @Override
    synchronized public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.d("MYTAG", "Touch Event");

        return gestureScanner.onTouchEvent(motionEvent);
    }

    @Override
    synchronized public boolean onDown(MotionEvent e) {
        Log.d("MYTAG", "DOWN");
        return false;
    }

    @Override
    synchronized public void onShowPress(MotionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    synchronized public boolean onSingleTapUp(MotionEvent e) {
        Log.d("MYTAG", "Jump");
        characterThread.jump();
        return true;
    }

    @Override
    synchronized public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("MYTAG", "Scroll");
        return false;
    }

    @Override
    synchronized public void onLongPress(MotionEvent e) {
        Log.d("MYTAG", "Long Press");

    }

    @Override
    synchronized public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityX > 0 && velocityX > Math.abs(velocityY)) {
            Log.d("MYTAG", "Sprint");
            characterThread.sprint();
            return true;
        } else if (velocityY > 0 && velocityY > Math.abs(velocityX)){
            Log.d("MYTAG", "Slide");
            characterThread.slide();
            return true;
        }
        Log.d("MYTAG", "Nothing");
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    synchronized private void endThreads() {
        try {
            characterThread.setRunning(false);
            characterThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
