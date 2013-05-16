package csci307.theGivingChild.CleanWaterGame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Extras_menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extras_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.extras_menu, menu);
		return true;
	}

}
