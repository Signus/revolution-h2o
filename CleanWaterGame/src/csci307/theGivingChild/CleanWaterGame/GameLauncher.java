/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is the main menu for the game play donate and extras
*
* History:
*    5/31/13 original
*/
 
package csci307.theGivingChild.CleanWaterGame;

import java.io.IOException;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameLauncher extends Activity {

    private boolean MUTE_SOUND_EFX;
    private final String gvingChildUrl = "http://www.thegivingchild.org/home/DONATE.html";
    public static final String PREFERENCE_KEY = "csci370.thegivingchild.cleanwatergame.preference";
    public static final String PREFERENCE_KEY_MUTE = "csci370.thegivingchild.cleanwatergame.preference.mute";
    public static final String PREFERENCE_KEY_INGAME = "csci370.thegivingchild.cleanwatergame.preference.ingame";
    public static final String PREFERENCE_KEY_INGAME_MUTE = "csci370.thegivingchild.cleanwatergame.preference.ingamemute";
   
   private boolean goingOtheract;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_launcher);
        MUTE_SOUND_EFX = CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false);
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	 goingOtheract = false;
    	ImageButton im = (ImageButton)findViewById(R.id.muting);
    	MUTE_SOUND_EFX = CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false);
    	im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute : R.drawable.unmuted));
    	CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false).commit();
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
        MUTE_SOUND_EFX = (CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false) ? false : true);
        im.setImageResource((MUTE_SOUND_EFX ? R.drawable.mute : R.drawable.unmuted));
        CleanWaterGame.getInstance().getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).edit().putBoolean(PREFERENCE_KEY_MUTE,MUTE_SOUND_EFX).commit();
        
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
