/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is the application class that is used for context and bug reporting
*
* History:
*    5/31/13 original
*/
package csci307.theGivingChild.CleanWaterGame;

import android.app.Application;
import android.media.MediaPlayer;

import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(
    formKey="",//depreciated not used but still required
    mailTo="ccard@mymail.mines.edu",
    customReportContent={ReportField.APP_VERSION_CODE, ReportField.ANDROID_VERSION, ReportField.STACK_TRACE},
    mode=ReportingInteractionMode.TOAST,
    resToastText=40
)
public class CleanWaterGame extends Application {
	private static CleanWaterGame instance;
	 private MediaPlayer menuMusic;
	 private MediaPlayer selectSound;
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		ACRA.init(this);
		
		 //Set menu music source
        try {
			menuMusic = MediaPlayer.create(this, R.raw.menumusic);
			menuMusic.setLooping(true);
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
	
	
	
	public static CleanWaterGame getInstance()
	{
		return instance;
	}
	
	public void pauseMenuMusic()
	{
		if(menuMusic.isPlaying())menuMusic.pause();
	}
	
	public void playMenuMusic()
	{
		menuMusic.start();
	}
	
	public void playBtnSound()
	{
		selectSound.start();
	}
}
