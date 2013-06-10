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


public class ActOneActivity extends BaseGameActivity {
	
	private BoundCamera camera;
	private ResourceManager resourceManager;
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
		boolean inLevel = MUTE_SOUND_EFX || CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false);

		if (!inLevel) {
			CleanWaterGame.getInstance().playMenuMusic();
		}
        if((!MUTE_SOUND_EFX) && CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false))
        {
        	ResourceManager.getInstance().backgroundMusic.play();
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
		if(!goingOtheract) CleanWaterGame.getInstance().pauseMenuMusic();
		ResourceManager.getInstance().backgroundMusic.pause();
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new BoundCamera(0, 0, 800, 480);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.camera);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
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
//		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
//		SceneManager.getInstance().createLevelSelectScene(pOnCreateSceneCallback);
		SceneManager.getInstance().createActSelectScene(pOnCreateSceneCallback);
		
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
//		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
//			
//			@Override
//			public void onTimePassed(TimerHandler pTimerHandler) {
//				mEngine.unregisterUpdateHandler(pTimerHandler);
//				SceneManager.getInstance().createMenuScene();				
//			}
//		}));
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
