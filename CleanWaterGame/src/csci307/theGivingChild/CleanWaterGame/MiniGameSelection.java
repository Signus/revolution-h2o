package csci307.theGivingChild.CleanWaterGame;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import csci307.theGivingChild.CleanWaterGame.LevelOne.LevelOneActivity;

public class MiniGameSelection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mini_game_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mini_game_selection, menu);
		return true;
	}

    public void openLevelOne(View v) {
        Intent levelOne = new Intent(this, LevelOneActivity.class);
        startActivity(levelOne);
    }
    public void openLevelTwo(View v) {
        Intent levelTwo = new Intent(this, ActOneActivity.class);
        startActivity(levelTwo);
    }
}
