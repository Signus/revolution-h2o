package csci307.theGivingChild.CleanWaterGame.LevelOne;

import android.app.Activity;
import android.os.Bundle;
import csci307.theGivingChild.CleanWaterGame.LevelOne.DrawingPanel;

public class LevelOneActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawingPanel(this));
    }
}