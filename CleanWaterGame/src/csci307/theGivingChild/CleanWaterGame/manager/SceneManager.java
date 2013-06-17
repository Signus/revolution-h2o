/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/22/13
 * Version: 1.0
 * Description: SceneManager is used to easily handle switching between scenes. Scenes should be loaded and disposed of properly. 
 */

package csci307.theGivingChild.CleanWaterGame.manager;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import csci307.theGivingChild.CleanWaterGame.scene.ActSelectScene;
import csci307.theGivingChild.CleanWaterGame.scene.AnimationScene;
import csci307.theGivingChild.CleanWaterGame.scene.AnimationScene.Animation;
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
	private BaseScene animationScene;
	
	
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
		SCENE_ACT_SELECT, 
		SCENE_ANIMATION
	}
	
	/**
	 * Accepts a BaseScene object, and makes it the currentScene
	 * @param scene : the BaseScene object that is to be the currentScene
	 */
	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}
	
	/**
	 * Accepts a SceneType object, and then calls setScene(BaseScene scene)
	 * @param sceneType : the type of scene that the currentScene will be switched to
	 */
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
			case SCENE_ANIMATION:
				setScene(animationScene);
				break;
			default:
				break;
		}
	}
	
	public void createLevelSelectScene() {
		ResourceManager.getInstance().loadMenuResources();
		levelSelectScene = new LevelSelectScene();
		loadingScene = new LoadingScene();
		currentScene = levelSelectScene;
	}
	
	public void createActSelectScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourceManager.getInstance().loadMenuResources();
		actSelectScene = new ActSelectScene();
		currentScene = actSelectScene;
		pOnCreateSceneCallback.onCreateSceneFinished(actSelectScene);
	}
		
	public void createAnimationScene(final Animation animation) {
		ResourceManager.getInstance().loadAnimationResources();
		animationScene = new AnimationScene(animation);
		currentScene = animationScene;
	}
	
	public void loadAnimationScene(final Engine mEngine) {
		ResourceManager.getInstance().unloadMenuGraphics();
		if (gameScene != null) {
			gameScene.disposeScene();
			ResourceManager.getInstance().unloadGameGraphics();
			gameScene = null;
		}
		if (levelSelectScene != null) {
			levelSelectScene.disposeScene();
			levelSelectScene = null;
		}
		mEngine.registerUpdateHandler(new TimerHandler(0.01f, new ITimerCallback() {
					
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					mEngine.unregisterUpdateHandler(pTimerHandler);
					setScene(animationScene);
			}
		}));
	}
	
	public void loadGameScene(final Engine mEngine, final String level, final String level2) {
		setScene(loadingScene);
		if (animationScene != null) {
			animationScene.disposeScene();
			ResourceManager.getInstance().unloadAnimationGraphics();
			animationScene = null;
		}
		mEngine.registerUpdateHandler(new TimerHandler(.1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();
				gameScene = new GameScene(level, level2);
				setScene(gameScene);
				
			}
		}));
	}
	
	public void loadActSelectScene(final Engine mEngine) {
		ResourceManager.getInstance().unloadMenuGraphics();
		if (levelSelectScene != null) {
			levelSelectScene.disposeScene();
			levelSelectScene = null;
		}
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
			gameScene = null;
		}
		if (animationScene != null) {
			setScene(loadingScene);
			animationScene.disposeScene();
			ResourceManager.getInstance().unloadAnimationGraphics();
			animationScene = null;
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
