/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is the menu that loads the AndEngine API resources and initiates the ActSelectScene
*
* History:
*    5/31/13 original
*/
package csci307.theGivingChild.CleanWaterGame;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;
import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.scene.GameScene;
import csci307.theGivingChild.CleanWaterGame.scene.GameScene.PausedType;


public class AndEngineGameActivity extends BaseGameActivity {
	
	private BoundCamera camera;
	private ResourceManager resourceManager;
	
	//This is  flag that helps determine if we are going to another activity or it is being put into the background
	private boolean goingOtheract;
	private boolean MUTE_SOUND_EFX;
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new FixedStepEngine(pEngineOptions, 60);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		goingOtheract = false;
		MUTE_SOUND_EFX = CleanWaterGame.getInstance()
				.getSharedPreferences(GameLauncher.PREFERENCE_KEY, MODE_MULTI_PROCESS)
				.getBoolean(GameLauncher.PREFERENCE_KEY_MUTE, false);
		//determines if we are suppose to mute the menu music because we are in a level or we are muted
		boolean inLevel = MUTE_SOUND_EFX || CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false);

		if (!inLevel) {
			CleanWaterGame.getInstance().playMenuMusic();
		}
		//determines if we are in a level or the sfx are muted
        if((!MUTE_SOUND_EFX) && CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false))
        {
        	CleanWaterGame.getInstance().playGameMusic();
        }
	}
	
	@Override
	public void onBackPressed()
	{
		goingOtheract = true;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		//if we aren't going to another activity then pause the menu music
		if (!goingOtheract) {
			CleanWaterGame.getInstance().pauseMenuMusic();
		}
		//if we are leaving the game for the home screen pause game music
		if(CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false))
		{
			CleanWaterGame.getInstance().pauseGameMusic();
		}
		GameScene.pausedType = PausedType.PAUSED_ON;
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new BoundCamera(0, 0, 800, 480);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.setUpdateThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws IOException {
		ResourceManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		resourceManager = ResourceManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)	throws IOException {
		SceneManager.getInstance().createActSelectScene(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}    
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (this.isGameLoaded())
		{
			System.exit(0);	
		}	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		return false;
	}
}
