package csci307.theGivingChild.CleanWaterGame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MiniGame_selection extends Activity {

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

}
