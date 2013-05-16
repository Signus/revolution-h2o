package csci307.theGivingChild.CleanWaterGame;

import android.app.Activity;
import android.os.Bundle;

public class LevelOneActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawingPanel(this));
    }
}