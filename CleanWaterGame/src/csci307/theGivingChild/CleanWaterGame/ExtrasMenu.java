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
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ExtrasMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extras_menu);
	}

	
	/**
	 * This will show the user the privacy policy
	 * @param the view calling this method
	 */
	public void showPolicy(View v)
	{
		Intent privacy = new Intent(this,PrivacyPolicy.class);
		startActivity(privacy);
	}
	
	/**
	 * This shows the credits for the app
	 * @param the view calling this method
	 */
	public void credits(View v)
	{
		Intent about = new Intent(this,AboutDialog.class);
		startActivity(about);
	}
	
	/**
	 * This opens the bug reporting / feed back dialog
	 * @param the view calling this
	 */
	public void feedBack(View v)
	{
		Intent userbug = new Intent(this, FeedBack.class);
		userbug.putExtra(FeedBack.ERROR_CODE_KEY,true);
		startActivity(userbug);
	}

}
