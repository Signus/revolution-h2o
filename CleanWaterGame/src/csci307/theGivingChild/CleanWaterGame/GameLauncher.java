/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/13/13
 * Description: This is the starting activity that the user will see whne starting the app
 */
 
package csci307.theGivingChild.CleanWaterGame;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class GameLauncher extends Activity {

    private boolean MUTE_SOUND_EFX=false;
    private final String gvingChildUrl = "http://www.thegivingchild.org/home/DONATE.html";
    public static final String PREFERENCE_KEY = "csci370.thegivingchild.cleanwatergame.preference";
    public static final String PREFERENCE_KEY_MUTE = "csci370.thegivingchild.cleanwatergame.preference.mute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_launcher);

    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	if(getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false) != MUTE_SOUND_EFX)
    	{
    		ImageButton im = (ImageButton)findViewById(R.id.muting);
    		 MUTE_SOUND_EFX = getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false);
    	     im.setImageResource((MUTE_SOUND_EFX ? R.drawable.unmuted : R.drawable.mute));
    	}
    }


    /**
    * This function is linked to the mute button in the activity_game_launcher.xml file
    * @param v The view that is calling this method
    */
    public void toggleMute(View v)
    {
        ImageButton im = (ImageButton)v;
        MUTE_SOUND_EFX = getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).getBoolean(PREFERENCE_KEY_MUTE, false);
        im.setImageResource((MUTE_SOUND_EFX ? R.drawable.unmuted : R.drawable.mute));
        getSharedPreferences(PREFERENCE_KEY, MODE_MULTI_PROCESS).edit().putBoolean(PREFERENCE_KEY_MUTE,(MUTE_SOUND_EFX ? false : true)).commit();
    }

    /**
    * This method is linked to the donate button in the activity
    * it opens the donation url in their phones web browser
    * @param v the view calling this method
    */
    public void openDonate(View v)
    {
        Intent donation = new Intent(Intent.ACTION_VIEW, Uri.parse(gvingChildUrl));
        startActivity(donation);
    }

    /**
    * This method opens the minigames selection activity and is linked
    * to the play game button
    * @param v the view calling this mehtod
    */
    public void openMiniGames(View v)
    {
        Intent play = new Intent(this, ActOneActivity.class);
        startActivity(play);
    }

    /**
    * This method opens the extras menu activity and is linked to the extras button
    * @param v the view calling this method
    */
    public void openExtras(View v)
    {
        Intent extra = new Intent(this, ExtrasMenu.class);
        startActivity(extra);
    }
    
}
