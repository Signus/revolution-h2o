package csci307.theGivingChild.CleanWaterGame.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import csci307.theGivingChild.CleanWaterGame.scene.BaseScene;
import csci307.theGivingChild.CleanWaterGame.scene.GameScene;
import csci307.theGivingChild.CleanWaterGame.scene.LevelSelectScene;

public class SceneManager {
	
	//-------------------------------------
	//SCENES
	//-------------------------------------
//	private BaseScene splashScene;
	private BaseScene menuScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	
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
		SCENE_LOADING
	}
	
	
	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType) {
		switch (sceneType) {
			case SCENE_MENU:
				setScene(menuScene);
				break;
			case SCENE_GAME:
				setScene(gameScene);
				break;
//			case SCENE_SPLASH:
//				setScene(splashScene);
//				break;
//			case SCENE_LOADING:
//				setScene(loadingScene);
//				break;
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
	
	public void createMenuScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourceManager.getInstance().loadMenuResources();
		menuScene = new LevelSelectScene();
		currentScene = menuScene;
		pOnCreateSceneCallback.onCreateSceneFinished(menuScene);
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
//		setScene(loadingScene);
		ResourceManager.getInstance().unloadMenuTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();
				gameScene = new GameScene(level);
				setScene(gameScene);
				
			}
		}));
	}
	
	public void loadMenuScene(final Engine mEngine) {
//		setScene(loadingScene);
		gameScene.disposeScene();
		ResourceManager.getInstance().unloadGameTextures();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuTextures();
				setScene(menuScene);
				
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
