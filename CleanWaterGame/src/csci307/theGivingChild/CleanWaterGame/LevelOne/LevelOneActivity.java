package csci307.theGivingChild.CleanWaterGame.LevelOne;

import android.app.Activity;
import android.os.Bundle;
import csci307.theGivingChild.CleanWaterGame.LevelOne.DrawingPanel;

public class LevelOneActivity extends Activity {
    DrawingPanel drawingPanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawingPanel = new DrawingPanel(this);
        setContentView(drawingPanel);
    }
}