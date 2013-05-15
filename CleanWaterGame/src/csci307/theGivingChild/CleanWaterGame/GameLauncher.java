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
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class GameLauncher extends Activity {

    public static boolean MUTE_SOUND_EFX=false;
    private final String gvingChildUrl = "http://www.thegivingchild.org/home/DONATE.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_launcher);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_launcher, menu);
        return true;
    }


    /**
    * This function is linked to the mute button in the activity_game_launcher.xml file
    * @param The view that is calling this method
    */
    public void toggleMute(View v)
    {
        ImageButton im = (ImageButton)v;

        im.setImageResource((MUTE_SOUND_EFX ? R.drawable.unmuted : R.drawable.mute));
        MUTE_SOUND_EFX = (MUTE_SOUND_EFX ? false : true);
    }

    /**
    * This method is linked to the donate button in the activity
    * it opens the donation url in their phones web browser
    * @param the view calling this method
    */
    public void openDonate(View v)
    {
        Intent donation = new Intent(Intent.ACTION_VIEW, Uri.parse(gvingChildUrl));
        startActivity(donation);
    }

    /**
    * This method opens the minigames selection activity and is linked
    * to the play game button
    * @param the view calling this mehtod
    */
    public void openMiniGames(View v)
    {
        Intent play = new Intent(this, MiniGame_selection.class);
        startActivity(play);
    }

    /**
    * This method opens the extras menu activity and is linked to the extras button
    * @param the view calling this method
    */
    public void openExtras(View v)
    {
        Intent extra = new Intent(this, Extras_menu.class);
        startActivity(extra);
    }
    
}
