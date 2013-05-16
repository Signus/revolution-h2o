package csci307.theGivingChild.CleanWaterGame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class PanelThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private DrawingPanel drawingPanel;
    private boolean run = false;

    public PanelThread(SurfaceHolder surfaceHolder, DrawingPanel drawingPanel) {
        this.surfaceHolder = surfaceHolder;
        this.drawingPanel = drawingPanel;
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
                    drawingPanel.postInvalidate();
                }
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

}
