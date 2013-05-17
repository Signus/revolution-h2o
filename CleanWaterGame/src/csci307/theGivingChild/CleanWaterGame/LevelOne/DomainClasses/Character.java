package csci307.theGivingChild.CleanWaterGame.LevelOne.DomainClasses;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import csci307.theGivingChild.CleanWaterGame.R;

public class Character {

    private Paint paint;
    private Point position;
    private Bitmap image;
    private Rect bounds;

    public Character(Resources resources, int x, int y) {
        paint = new Paint();
        position = new Point(x, y);
        image = BitmapFactory.decodeResource(resources, R.drawable.character);
        bounds = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    synchronized public void draw(Canvas canvas) {
        if (position.x > 0) {
            canvas.drawBitmap(image, position.x, position.y, null);
        }
    }
}
