package csci307.theGivingChild.CleanWaterGame.LevelOne;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.Random;

import csci307.theGivingChild.CleanWaterGame.LevelOne.DomainClasses.GameCharacter;

public class CharacterThread extends Thread {

    public static final float SPEED = 4;
    public static final float RADIUS = 50;
    public static final Random rnd = new Random();
    private SurfaceHolder surfaceHolder;
    private DrawingPanel drawingPanel;
    private GameCharacter character;
    private boolean run = false;

    public CharacterThread(SurfaceHolder surfaceHolder, DrawingPanel drawingPanel) {
        this.surfaceHolder = surfaceHolder;
        this.drawingPanel = drawingPanel;
        character = new GameCharacter(drawingPanel.getResources(), drawingPanel.getWidth() / 4, 5 * drawingPanel.getHeight() / 8);
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
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public void doDraw(Canvas canvas) {
        character.doDraw(canvas);
    }

    public void jump() {
        character.setIsJumping(true);
    }
}
