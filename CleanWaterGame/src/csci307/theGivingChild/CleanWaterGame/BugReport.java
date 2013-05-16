/**
* Authors: Maria, Chris, Tony, Gurpreet, Dylan, Dustin
* Date: 5/16/13
* Verson: 1.0
* Descripion: this activity will show up as a dilog and allow for error reporting
*
* Note: depending on the intent (system reprot v user report) youll pass in a boolean
*   true for user report and false for system report
*/
package csci307.theGivingChild.CleanWaterGame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BugReport extends Activity {

	public static final String ERROR_CODE_KEY = "csci370.theGivingChild.Error.boolerror";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(savedInstanceState != null)
		{
			if (savedInstanceState.getBoolean(ERROR_CODE_KEY,false)) 
			{
				setContentView(R.layout.userbugreport);
			}
			else
			{
				setContentView(R.layout.systembugreport);
			}
			
		}
		else
		{
			//error fun
		}
	}




}
