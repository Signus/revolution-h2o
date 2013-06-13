package csci307.theGivingChild.CleanWaterGame.scene;

import java.util.ArrayList;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.adt.color.Color;

import csci307.theGivingChild.CleanWaterGame.CleanWaterGame;
import csci307.theGivingChild.CleanWaterGame.GameLauncher;
import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;

public class AnimationScene extends BaseScene implements IOnMenuItemClickListener {
	
	private Animation currentAnimation;
	
	private final int MENU_NEXT = 0;
	private final int MENU_PREV = 1;
	private final int MENU_SKIP = 2;
	private final int MENU_QUIT = 3;
//	private final Sprite[] scene_one = new Sprite[8];
	private ArrayList<Sprite> scene_one;
	private int currentScene;
	
	public enum Animation {
		SCENE_ONE,
		SCENE_TWO,
		SCENE_THREE,
		SCENE_FOUR,
		SCENE_FIVE,
		SCENE_SIX
	}

	public AnimationScene(Animation animation) {
    	this.engine = ResourceManager.getInstance().engine;
        this.activity = ResourceManager.getInstance().activity;
        this.vbom = ResourceManager.getInstance().vbom;
        this.camera = ResourceManager.getInstance().camera;
        currentAnimation = animation;
        currentScene = 0;
        createScene();
        loadAnimation(animation);		
	}
	

	@Override
	public void createScene() {
			
		createMenuChildScene();
		if(ResourceManager.getInstance().backgroundMusic.isPlaying())ResourceManager.getInstance().backgroundMusic.pause();
		ResourceManager.getInstance().backgroundMusic.play();
		if (ResourceManager.getInstance().isMuted()) {
			ResourceManager.getInstance().backgroundMusic.pause();
		}
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_ANIMATION;
	}

	@Override
	public void disposeScene() {
		if(ResourceManager.getInstance().backgroundMusic.isPlaying())ResourceManager.getInstance().backgroundMusic.stop();
		
	}
	
	private void loadAnimation(Animation animation) {
//		currentAnimation = Animation.SCENE_ONE;
		switch (animation) {
			case SCENE_ONE:
				displaySceneOne();
				break;
			case SCENE_TWO:
				displaySceneTwo();
				break;
			case SCENE_THREE:
				break;
			case SCENE_FOUR:
				break;
			case SCENE_FIVE:
				break;
			case SCENE_SIX:
				break;
			
		}		
	}
	
	private void createMenuChildScene() {
		final MenuScene navigation = new MenuScene(camera);

//		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, resourcesManager.font, "QUIT", vbom), Color.RED, Color.WHITE);
		final IMenuItem skipMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_SKIP, ResourceManager.getInstance().font, "SKIP", vbom), Color.RED, Color.WHITE);
		final IMenuItem nextMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_NEXT, ResourceManager.getInstance().font, "NEXT", vbom), Color.RED, Color.WHITE);
		final IMenuItem previousMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_PREV, ResourceManager.getInstance().font, "PREV", vbom), Color.RED, Color.WHITE);

//		gameOver.attachChild(new Text(400, 400, resourcesManager.game_font, "GAME OVER", vbom));
		nextMenuItem.setPosition(740, 20);
		previousMenuItem.setPosition(60, 20);
		skipMenuItem.setPosition(740, 70);
//		quitMenuItem.setPosition(60, 460);
		
		navigation.addMenuItem(skipMenuItem);
//		navigation.addMenuItem(quitMenuItem);
		navigation.addMenuItem(nextMenuItem);
		navigation.addMenuItem(previousMenuItem);
		
		navigation.setBackgroundEnabled(false);
		navigation.setOnMenuItemClickListener(this);
		
		setChildScene(navigation);
//		return navigation;
	}
	
	private void displaySceneOne() {
		scene_one = new ArrayList<Sprite>();
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_one, vbom));
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_two, vbom));
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_three, vbom));
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_four, vbom));
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_five, vbom));
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_six, vbom));
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_seven, vbom));
		scene_one.add(new Sprite(400, 240, ResourceManager.getInstance().animation_one_eight, vbom));
		attachChild(scene_one.get(currentScene));
	}

	private void displaySceneTwo() {
		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_SKIP:
				switch (currentAnimation) {
					case SCENE_ONE:
						disposeScene();
						if(ResourceManager.getInstance().backgroundMusic.isPlaying())ResourceManager.getInstance().backgroundMusic.pause();
						SceneManager.getInstance().loadGameScene(engine, "act1scene1", "act1scene2");
						CleanWaterGame.getInstance().pauseMenuMusic();
						CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, true).commit();						
						break;
					case SCENE_TWO:
						disposeScene();
						if(ResourceManager.getInstance().backgroundMusic.isPlaying())ResourceManager.getInstance().backgroundMusic.pause();
						SceneManager.getInstance().loadGameScene(engine, "act1scene2", "act1scene3");
						CleanWaterGame.getInstance().pauseMenuMusic();
						CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, true).commit();
					default:
						break;
				}
					
				break;
			case MENU_PREV:
				switch (currentAnimation) {
					case SCENE_ONE:
						if (currentScene != 0) {
							detachChild(scene_one.get(currentScene));
							currentScene--;
							attachChild(scene_one.get(currentScene));
						} else {
							disposeScene();
							SceneManager.getInstance().loadMenuScene(engine);
						}
						
						break;
					case SCENE_TWO:
						if (currentScene != 0) {
						} else {
							disposeScene();
							SceneManager.getInstance().loadMenuScene(engine);
						}
						
						break;
					default:
						break;
				}
				break;
			case MENU_NEXT:
				switch (currentAnimation) {
					case SCENE_ONE:
						if (currentScene != scene_one.size() - 1) {
							detachChild(scene_one.get(currentScene));
							currentScene++;
							attachChild(scene_one.get(currentScene));
						} else {
							disposeScene();
							if(ResourceManager.getInstance().backgroundMusic.isPlaying())ResourceManager.getInstance().backgroundMusic.pause();
							SceneManager.getInstance().loadGameScene(engine, "act1scene1", "act1scene2");
							CleanWaterGame.getInstance().pauseMenuMusic();
							CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, true).commit();
						}
						
						break;
					case SCENE_TWO:
						if (currentScene != scene_one.size() - 1) {
							
						} else {
							disposeScene();
							if(ResourceManager.getInstance().backgroundMusic.isPlaying())ResourceManager.getInstance().backgroundMusic.pause();
							SceneManager.getInstance().loadGameScene(engine, "act1scene2", "act1scene3");
							CleanWaterGame.getInstance().pauseMenuMusic();
							CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, true).commit();
						}
						
						break;
					default:
						break;
				}
				break;
			default:
				return false;
		}
		return false;
	}

}
