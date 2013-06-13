/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is the application class that is used for context and bug reporting and is a
*              singleton patter to facilitate getting the context and playing music 
*
* History:
*    5/31/13 original 1.0
*/
package csci307.theGivingChild.CleanWaterGame;

import android.app.Application;
import android.media.MediaPlayer;

import org.acra.*;
import org.acra.annotation.*;

//This is the acra code for the bug reporting it defines what will happen when a exception occurs
//and what is sent
@ReportsCrashes(
    formKey="",//depreciated not used but still required
    mailTo="thegivingchild@gmail.com",
    customReportContent={ReportField.APP_VERSION_CODE, ReportField.ANDROID_VERSION, ReportField.STACK_TRACE},
    mode=ReportingInteractionMode.TOAST,
    resToastText=40
)
public class CleanWaterGame extends Application {
	private static CleanWaterGame instance;
	 private MediaPlayer menuMusic;
	 private MediaPlayer gameMusic;
	 private MediaPlayer selectSound;
	 
	 
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		//This initializes the bug reporting
		ACRA.init(this);
		
		 //Set menu music source
        try {
			menuMusic = MediaPlayer.create(this, R.raw.menumusic);
			menuMusic.setLooping(true);
			gameMusic = MediaPlayer.create(this, R.raw.gamemusic);
			gameMusic.setLooping(true);
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
	 * This gets the current instance of CleanWaterGame
	 * @return an instance of this class
	 */
	public static CleanWaterGame getInstance()
	{
		return instance;
	}
	
	/**
	 * This method pauses the menu music
	 */
	public void pauseMenuMusic()
	{
		//If this check is not done it results in an error
		//the mediaplayer can only be paused when it is playing music
		if (menuMusic.isPlaying()) {
			menuMusic.pause();
		}
	}
	public void pauseGameMusic()
	{
		//If this check is not done it results in an error
		//the mediaplayer can only be paused when it is playing music
		if (gameMusic.isPlaying()) {
			gameMusic.pause();
		}
	}
	
	/**
	 * starts the looped menu music media player
	 */
	public void playMenuMusic()
	{
		menuMusic.start();
	}
	
	public void playGameMusic()
	{
		gameMusic.start();
	}
	
	/**
	 * Starts the button sound media player 
	 */
	public void playBtnSound()
	{
		selectSound.start();
	}
}
