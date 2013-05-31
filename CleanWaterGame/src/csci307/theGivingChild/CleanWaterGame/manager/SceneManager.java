/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/22/13
 * Description: Scene Manager is used to easily handle switching between scenes. Scenes should be loaded and disposed of properly. 
 */

package csci307.theGivingChild.CleanWaterGame.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import csci307.theGivingChild.CleanWaterGame.scene.ActSelectScene;
import csci307.theGivingChild.CleanWaterGame.scene.BaseScene;
import csci307.theGivingChild.CleanWaterGame.scene.GameScene;
import csci307.theGivingChild.CleanWaterGame.scene.LevelSelectScene;
import csci307.theGivingChild.CleanWaterGame.scene.LoadingScene;

public class SceneManager {
	
	//-------------------------------------
	//SCENES
	//-------------------------------------
//	private BaseScene splashScene;
	private BaseScene levelSelectScene;
	private BaseScene gameScene;
	private BaseScene actSelectScene;
	private BaseScene loadingScene;
	
	
	//
	//-------------------------------------
	//VARIABLES
	//-------------------------------------
	private static final SceneManager INSTANCE = new SceneManager();
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	private BaseScene currentScene;
	private Engine engine = ResourceManager.getInstance().engine;
	
	public enum SceneType {
		SCENE_SPLASH, 
		SCENE_MENU,
		SCENE_GAME,
		SCENE_LOADING,
		SCENE_LEVEL_SELECT,
		SCENE_ACT_SELECT
	}
	
	
	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType) {
		switch (sceneType) {
			case SCENE_ACT_SELECT:
				setScene(actSelectScene);
				break;
			case SCENE_GAME:
				setScene(gameScene);
				break;
			case SCENE_LEVEL_SELECT:
				setScene(levelSelectScene);
				break;
			case SCENE_LOADING:
				setScene(loadingScene);
				break;
			default:
				break;
		}
	}
	
//	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
//		ResourceManager.getInstance().loadSplashScreen();
//		splashScene = new SplashScene();
//		currentScene = splashScene;
//		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
//	}
	
	public void createLevelSelectScene() {
		ResourceManager.getInstance().loadMenuResources();
		levelSelectScene = new LevelSelectScene();
		loadingScene = new LoadingScene();
		currentScene = levelSelectScene;
//		pOnCreateSceneCallback.onCreateSceneFinished(levelSelectScene);
	}
	
	public void createActSelectScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourceManager.getInstance().loadMenuResources();
		actSelectScene = new ActSelectScene();
		currentScene = actSelectScene;
		pOnCreateSceneCallback.onCreateSceneFinished(actSelectScene);
	}
//	public void createMenuScene() {
//		ResourceManager.getInstance().loadMenuResources();
//		menuScene = new LevelSelectScene();
//		loadingScene = new LoadingScene();
//		SceneManager.getInstance().setScene(menuScene);
//		disposeSplashScene();
//	}
	
	
//	private void disposeSplashScene() {
//		ResourceManager.getInstance().unloadSplashScreen();
//		splashScene.disposeScene();
//		splashScene = null;
//	}
	
	public void loadGameScene(final Engine mEngine, final String level) {
		setScene(loadingScene);
		ResourceManager.getInstance().unloadMenuGraphics();
		mEngine.registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();
				gameScene = new GameScene(level);
				setScene(gameScene);
				
			}
		}));
	}
	
	public void loadActSelectScene(final Engine mEngine) {
		ResourceManager.getInstance().unloadMenuGraphics();
		mEngine.registerUpdateHandler(new TimerHandler(0.01f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuResources();
				setScene(actSelectScene);
			}
		}));
	}
	
	public void loadMenuScene(final Engine mEngine) {
		if (gameScene != null) {
			setScene(loadingScene);
			gameScene.disposeScene();
			ResourceManager.getInstance().unloadGameGraphics();
		}
		
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuGraphics();
				setScene(levelSelectScene);
				
			}
		}));
	}
	//-------------------------------------
	//GETTERS & SETTERS
	//-------------------------------------
	
	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene() {
		return currentScene;
	}
}
