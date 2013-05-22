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
    public static final int SLIDE_TIME = 100;
    public static final int SPRINT_TIME = 100;
    public int action_time = 0;

    private enum Image {
        INITIAL_IMAGE, SLIDING_IMAGE, JUMPING_IMAGE, SPRINTING_IMAGE;

        private Bitmap image;

        public void setImage(Bitmap im) {
            image = im;
        }

        public Bitmap getImage() {
            return image;
        }

    }

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

        image = Image.INITIAL_IMAGE.getImage();
        bounds = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    private void readInImages(Resources resources) {
        Image.INITIAL_IMAGE.setImage(BitmapFactory.decodeResource(resources, R.drawable.character));
        Image.SLIDING_IMAGE.setImage(BitmapFactory.decodeResource(resources, R.drawable.character_slide));
        Image.JUMPING_IMAGE.setImage(BitmapFactory.decodeResource(resources, R.drawable.character_jump));
        Image.SPRINTING_IMAGE.setImage(BitmapFactory.decodeResource(resources, R.drawable.character_sprint));
    }

    synchronized public void doDraw(Canvas canvas) {
        if (canvas == null) return;
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
            image = Image.INITIAL_IMAGE.getImage();
            jumpDirection = -1;
        }
        if (position.y <= initialPosition.y - JUMP_HEIGHT) {
            jumpDirection = 1;
        }

        position.y += jumpDirection;
    }

    private void performSlide() {
        if (action_time >= SLIDE_TIME) {
            isSliding = false;
            image = Image.INITIAL_IMAGE.getImage();
            action_time = 0;
        } else {
            action_time++;
            image = Image.SLIDING_IMAGE.getImage();
        }
    }

    private void performSprint() {
        if (action_time >= SPRINT_TIME) {
            isSprinting = false;
            image = Image.INITIAL_IMAGE.getImage();
            action_time = 0;
        } else {
            action_time++;
            image = Image.SPRINTING_IMAGE.getImage();
        }
    }

    public void setIsJumping(boolean b) {
        if (noActions()) {
            image = Image.JUMPING_IMAGE.getImage();
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
