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

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class ExtrasMenu extends Activity {
	private boolean MUTE_SOUND_EFX;
    public static final String PREFERENCE_KEY = "csci370.thegivingchild.cleanwatergame.preference";
    public static final String PREFERENCE_KEY_MUTE = "csci370.thegivingchild.cleanwatergame.preference.mute";
    public static MediaPlayer selectSound;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extras_menu);
		
        MUTE_SOUND_EFX = CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false);
        
        //Set menu music source
        try {
			selectSound = MediaPlayer.create(this, R.raw.select_button);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}

	
	/**
	 * This will show the user the privacy policy
	 * @param the view calling this method
	 */
	public void showPolicy(View v)
	{
        if (!MUTE_SOUND_EFX) {
        	selectSound.start();
        }
		Intent privacy = new Intent(this,PrivacyPolicy.class);
		startActivity(privacy);
	}
	
	/**
	 * This shows the credits for the app
	 * @param the view calling this method
	 */
	public void credits(View v)
	{
        if (!MUTE_SOUND_EFX) {
        	selectSound.start();
        }
		Intent about = new Intent(this,AboutDialog.class);
		startActivity(about);
	}
	
	/**
	 * This opens the bug reporting / feed back dialog
	 * @param the view calling this
	 */
	public void feedBack(View v)
	{
        if (!MUTE_SOUND_EFX) {
        	selectSound.start();
        }
		Intent userbug = new Intent(this, FeedBack.class);
		userbug.putExtra(FeedBack.ERROR_CODE_KEY,true);
		startActivity(userbug);
	}

}
