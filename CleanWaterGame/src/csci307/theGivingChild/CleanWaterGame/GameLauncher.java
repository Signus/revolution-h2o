/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/13/13
 * Description: This is the starting activity that the user will see whne starting the app
 */
 
package csci307.theGivingChild.CleanWaterGame;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class GameLauncher extends Activity {

    public static boolean MUTE_SOUND_EFX=false;
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
    
}
