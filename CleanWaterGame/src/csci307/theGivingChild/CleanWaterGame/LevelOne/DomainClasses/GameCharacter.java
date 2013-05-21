package csci307.theGivingChild.CleanWaterGame.LevelOne.DomainClasses;

import csci307.theGivingChild.CleanWaterGame.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;



public class GameCharacter {

    public static final int JUMP_HEIGHT = 100;
    private static Bitmap INITIAL_IMAGE;
    private static Bitmap SLIDING_IMAGE;
    private static Bitmap JUMPING_IMAGE;
    private static Bitmap SPRINTING_IMAGE;

    private Point initialPosition;
    private Paint paint;
    private Point position;
    private Bitmap image;
    private Rect bounds;
    private boolean isJumping = false;
    private int jumpDirection = -1;
    private boolean isSprinting = false;
    private boolean isSliding = false;

    public GameCharacter(Resources resources, int x, int y) {
        paint = new Paint();
        initialPosition = new Point(x, y);
        position = new Point(x, y);

        readInImages(resources);

        image = INITIAL_IMAGE;
        bounds = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    private void readInImages(Resources resources) {
        INITIAL_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.character);
        SLIDING_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.character_slide);
        JUMPING_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.character_jump);
        SPRINTING_IMAGE = BitmapFactory.decodeResource(resources, R.drawable.character_sprint);
    }

    synchronized public void doDraw(Canvas canvas) {
        if (position.x > 0) {
            performAction();
            canvas.drawBitmap(image, position.x, position.y, null);
        }
    }

    public synchronized void performAction() {

        if (isJumping)
            performJump();
        else if (isSliding)
            performSlide();
        else if (isSprinting)
            performSprint();
    }

    private void performJump() {
        if (position.y >= initialPosition.y) {
            isJumping = false;
            image = INITIAL_IMAGE;
            jumpDirection = -1;
        }
        if (position.y <= initialPosition.y - JUMP_HEIGHT) {
            jumpDirection = 1;
        }

        position.y += jumpDirection;
    }

    private void performSlide() {
        image = SLIDING_IMAGE;
    }

    private void performSprint() {
        image = SPRINTING_IMAGE;

    }

    public void setIsJumping(boolean b) {
        if (noActions()) {
            image = JUMPING_IMAGE;
            isJumping = b;
        }
    }

    public void setIsSliding(boolean sliding) {
        if (noActions()) {
            isSliding = sliding;
        }
    }

    public void setIsSprinting(boolean sprinting) {
        if (noActions()) {
            isSprinting = sprinting;
        }
    }

    private synchronized boolean noActions() {
        return !(isSprinting || isSliding || isJumping);
    }

}
