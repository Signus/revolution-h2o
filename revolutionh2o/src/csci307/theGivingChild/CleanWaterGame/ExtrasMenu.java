/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
 * Date: 5/15/13
 * Version: 1.0
 * Description: This is the background file for the extras menu
 * 
 * History:
 *  5/15/13 original 1.0
 */

package csci307.theGivingChild.CleanWaterGame;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ExtrasMenu extends Activity {
	private boolean MUTE_SOUND_EFX;
	//This is used to tell if we are going to another activity within this app or the whole app is being
	//put into the background
	private boolean goingOtheract;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extras_menu);

	}

	@Override
	public void onResume() {
		super.onResume();
		goingOtheract = false;
		ImageButton im = (ImageButton) findViewById(R.id.extr_muting);
		//gets the apps current mute state and sets the mute/unmute icon appropriately
		MUTE_SOUND_EFX = CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_MUTE, false);
		im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute : R.drawable.unmuted));

		if (!MUTE_SOUND_EFX) {
			CleanWaterGame.getInstance().playMenuMusic();
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		//if we aren't going to another activity pause the music
		if(!goingOtheract) {
			CleanWaterGame.getInstance().pauseMenuMusic();
		}
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		goingOtheract = true;
	}

	/**
	 * This function is linked to the mute button in the
	 * activity_game_launcher.xml file
	 * 
	 * @param The view that is calling this method
	 */
	public void toggleMute(View v) {
		ImageButton im = (ImageButton) v;
		
		//when the mute/unmute icon is clicked this changes the apps mute preference and modifies the icon to the appropriate image
		MUTE_SOUND_EFX = (CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, MODE_MULTI_PROCESS)
				.getBoolean(GameLauncher.PREFERENCE_KEY_MUTE, false) ? false : true);
		
		im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute : R.drawable.unmuted));
		
		CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, MODE_MULTI_PROCESS)
				.edit().putBoolean(GameLauncher.PREFERENCE_KEY_MUTE, MUTE_SOUND_EFX)
				.commit();

		//Then pauses or plays the music as desired
		if (!MUTE_SOUND_EFX) {
			CleanWaterGame.getInstance().playMenuMusic();
		} else {
			CleanWaterGame.getInstance().pauseMenuMusic();
		}
	}

	/**
	 * This will show the user the privacy policy
	 * 
	 * @param the view calling this method
	 */
	public void showPolicy(View v) {
		if (!MUTE_SOUND_EFX) {
			CleanWaterGame.getInstance().playBtnSound();
		}
		Intent privacy = new Intent(this, PrivacyPolicy.class);
		startActivity(privacy);
	}

	/**
	 * This shows the credits for the app
	 * 
	 * @param the
	 *            view calling this method
	 */
	public void credits(View v) {
		if (!MUTE_SOUND_EFX) {
			CleanWaterGame.getInstance().playBtnSound();
		}
		Intent about = new Intent(this, AboutDialog.class);
		startActivity(about);
	}

	/**
	 * This opens the bug reporting / feed back dialog
	 * 
	 * @param the
	 *            view calling this
	 */
	public void feedBack(View v) {
		if (!MUTE_SOUND_EFX) {
			CleanWaterGame.getInstance().playBtnSound();
		}
		Intent userbug = new Intent(this, FeedBack.class);
		startActivity(userbug);
	}

}
