/**
 * Authors: Chris, Tony, Gurpreet, Dylan, Dustin
 * Date: 5/15/13
 * Version: 1.0
 * Description: This is the background file for the extras menu
 * 
 * History:
 *   original 1.0
 */

package csci307.theGivingChild.CleanWaterGame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class Extras_menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extras_menu);
	}

	/**
	 * for testing purposes for error reporting
	 */
	public void testReport(View v)
	{
		
	}

}
