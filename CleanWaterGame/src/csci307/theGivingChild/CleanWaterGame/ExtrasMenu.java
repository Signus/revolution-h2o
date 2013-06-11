/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
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
import android.widget.ImageButton;

public class ExtrasMenu extends Activity {
	private boolean MUTE_SOUND_EFX;
	public static final String PREFERENCE_KEY = "csci370.thegivingchild.cleanwatergame.preference";
	public static final String PREFERENCE_KEY_MUTE = "csci370.thegivingchild.cleanwatergame.preference.mute";
	public static MediaPlayer selectSound;
	private boolean goingOtheract;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_extras_menu);

		MUTE_SOUND_EFX = CleanWaterGame.getInstance()
				.getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS)
				.getBoolean(PREFERENCE_KEY_MUTE, false);

		// Set menu music source
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

	@Override
	public void onResume() {
		super.onResume();
		goingOtheract = false;
		ImageButton im = (ImageButton) findViewById(R.id.extr_muting);
		MUTE_SOUND_EFX = CleanWaterGame.getInstance()
				.getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS)
				.getBoolean(PREFERENCE_KEY_MUTE, false);
		im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute
				: R.drawable.unmuted));

		if (!MUTE_SOUND_EFX) {
			CleanWaterGame.getInstance().playMenuMusic();
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		if(!goingOtheract) CleanWaterGame.getInstance().pauseMenuMusic();
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
	 * @param v
	 *            The view that is calling this method
	 */
	public void toggleMute(View v) {
		ImageButton im = (ImageButton) v;
		MUTE_SOUND_EFX = (CleanWaterGame.getInstance()
				.getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS)
				.getBoolean(PREFERENCE_KEY_MUTE, false) ? false : true);
		im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute
				: R.drawable.unmuted));
		CleanWaterGame.getInstance()
				.getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS)
				.edit().putBoolean(PREFERENCE_KEY_MUTE, MUTE_SOUND_EFX)
				.commit();

		if (!MUTE_SOUND_EFX) {
			CleanWaterGame.getInstance().playMenuMusic();
		} else {
			CleanWaterGame.getInstance().pauseMenuMusic();
		}
	}

	/**
	 * This will show the user the privacy policy
	 * 
	 * @param the
	 *            view calling this method
	 */
	public void showPolicy(View v) {
		if (!MUTE_SOUND_EFX) {
			selectSound.start();
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
			selectSound.start();
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
			selectSound.start();
		}
		Intent userbug = new Intent(this, FeedBack.class);
		userbug.putExtra(FeedBack.ERROR_CODE_KEY, true);
		startActivity(userbug);
	}

}
