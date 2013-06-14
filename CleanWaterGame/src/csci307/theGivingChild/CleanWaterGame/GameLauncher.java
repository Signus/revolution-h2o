/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is the main menu that displays the Play, Extras and Donate buttons.
*
* History:
*    5/31/13 original 1.0
*/

package csci307.theGivingChild.CleanWaterGame;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GameLauncher extends Activity {

    private boolean MUTE_SOUND_EFX;
    private final String gvingChildUrl = "http://www.thegivingchild.org/home/DONATE.html";

    //These are keys for the shared preference that is used through out this app
    //This is the mute preference for the app
    public static final String PREFERENCE_KEY = "csci370.thegivingchild.cleanwatergame.preference";
    public static final String PREFERENCE_KEY_MUTE = "csci370.thegivingchild.cleanwatergame.preference.mute";

    //this allows us to detect if we are in a scene and determine what music to play
    public static final String PREFERENCE_KEY_INGAME = "csci370.thegivingchild.cleanwatergame.preference.ingame";
    public static final String PREFERENCE_KEY_INGAME_MUTE = "csci370.thegivingchild.cleanwatergame.preference.ingamemute";
    public static final String LOCK_LEVELS = "csci370.thegivingchild.cleanwatergame.preference.levellocks";

    //This tells us if we are going to another activity in this app or the app is being put into the background
   private boolean goingOtheract;
   
   private final static int INVISIBLE=4;
   private final static int VISIBLE=0;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_launcher);
        
        ((LinearLayout)findViewById(R.id.main_layout)).setVisibility(INVISIBLE);
        ((ImageButton)findViewById(R.id.muting)).setVisibility(INVISIBLE);
        
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
               ((LinearLayout)findViewById(R.id.main_layout)).setVisibility(VISIBLE);
               ((ImageButton)findViewById(R.id.muting)).setVisibility(VISIBLE);
            }
        }, 1500);
    }

    @Override
    public void onResume(){
    	super.onResume();
    	 goingOtheract = false;
    	ImageButton im = (ImageButton)findViewById(R.id.muting);

    	//sets up the mute/unmute icon to the appropriate icon
    	MUTE_SOUND_EFX = CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false);

    	im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute : R.drawable.unmuted));

    	//This ensures that the ingame preference is false when we start other wise an error occurs
    	CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit()
    			.putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false).commit();

    	if (!MUTE_SOUND_EFX) {
    		CleanWaterGame.getInstance().playMenuMusic();
    	}
    }

    @Override
    public void onPause()
    {
    	super.onPause();

    	if(!goingOtheract)
    	{
    		CleanWaterGame.getInstance().pauseMenuMusic();
    	}
    }

    /**
    * This function is linked to the mute button in the activity_game_launcher.xml file
    * @param v The view that is calling this method
    */
    public void toggleMute(View v)
    {
        ImageButton im = (ImageButton)v;

        //sets mute_sound_efex and the mute/unmute icon to the appropriate values as well as the apps mute preference
        MUTE_SOUND_EFX = (CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false) ? false : true);

        im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute : R.drawable.unmuted));

        CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).edit().putBoolean(PREFERENCE_KEY_MUTE,MUTE_SOUND_EFX).commit();

        //plays and pauses the music as desired
        if (!MUTE_SOUND_EFX) {
        	CleanWaterGame.getInstance().playMenuMusic();
        }
        else {
        	CleanWaterGame.getInstance().pauseMenuMusic();
        }
    }

    /**
    * This method is linked to the donate button in the activity
    * it opens the donation url in their phones web browser
    * @param v the view calling this method
    */
    public void openDonate(View v)
    {
    	 goingOtheract = true;
        if (!MUTE_SOUND_EFX) {
        	CleanWaterGame.getInstance().playBtnSound();
        }
        Intent donation = new Intent(Intent.ACTION_VIEW, Uri.parse(gvingChildUrl));
        startActivity(donation);
    }

    /**
    * This method opens the minigames selection activity and is linked
    * to the play game button
    * @param v the view calling this method
    */
    public void openMiniGames(View v)
    {
    	 goingOtheract = true;
        if (!MUTE_SOUND_EFX) {
        	CleanWaterGame.getInstance().playBtnSound();
        }
        Intent play = new Intent(this, AndEngineGameActivity.class);
        startActivity(play);
    }

    /**
    * This method opens the extras menu activity and is linked to the extras button
    * @param v the view calling this method
    */
    public void openExtras(View v)
    {
    	 goingOtheract = true;
        if (!MUTE_SOUND_EFX) {
        	CleanWaterGame.getInstance().playBtnSound();
        }
        Intent extra = new Intent(this, ExtrasMenu.class);
        startActivity(extra);
    }

}
