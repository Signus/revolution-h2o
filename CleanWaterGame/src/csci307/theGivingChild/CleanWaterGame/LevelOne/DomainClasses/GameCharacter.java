package csci307.theGivingChild.CleanWaterGame.LevelOne.DomainClasses;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import csci307.theGivingChild.CleanWaterGame.R;

public class GameCharacter {

    public static final int JUMP_HEIGHT = 100;
    private Point initialPosition;
    private Paint paint;
    private Point position;
    private Bitmap image;
    private Rect bounds;
    private boolean isJumping = false;
    private int jumpDirection = -1;

    public GameCharacter(Resources resources, int x, int y) {
        paint = new Paint();
        initialPosition = new Point(x, y);
        position = new Point(x, y);
        image = BitmapFactory.decodeResource(resources, R.drawable.character);
        bounds = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    synchronized public void doDraw(Canvas canvas) {
        if (position.x > 0) {
            performJump();
            canvas.drawBitmap(image, position.x, position.y, null);
        }
    }

    private void performJump() {
        if (!isJumping) return;
        if (position.y >= initialPosition.y) {
            isJumping = false;
            jumpDirection = -1;
        }
        if (position.y <= initialPosition.y - JUMP_HEIGHT) {
            jumpDirection = 1;
        }

        position.y += jumpDirection;

    }

    public void setIsJumping(boolean b) {
        isJumping = b;
    }
}
