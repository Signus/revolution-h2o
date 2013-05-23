package csci307.theGivingChild.CleanWaterGame.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;

public class LevelSelectScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menuChildScene;
	
	private final int SCENE_ONE = 0;
	private final int SCENE_TWO = 1;
	
	
	@Override
	public boolean onMenuItemClicked(org.andengine.entity.scene.menu.MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case SCENE_ONE:
				SceneManager.getInstance().loadGameScene(engine, "act1scene1");
				return true;
			case SCENE_TWO:
				return true;
			default:
				return false;
		}
	}
	

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();		
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	private void createBackground() {
		setBackground(new Background(Color.BLACK));
		attachChild(new Text(400, camera.getHeight() - 40, resourcesManager.font, "Act I", vbom));
	}
	
	private void createMenuChildScene() {
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0, 0);
		
//		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_button_TR, vbom), 1.2f, 1);
//		final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_OPTIONS, resourcesManager.options_button_TR, vbom), 1.2f, 1);
		
		final IMenuItem levelOneMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(SCENE_ONE, resourcesManager.scene_one_TR, vbom), 1.2f, 1);
		
//		menuChildScene.addMenuItem(playMenuItem);
//		menuChildScene.addMenuItem(optionsMenuItem);
		
		menuChildScene.addMenuItem(levelOneMenuItem);
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
//		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() + 10);
//		optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() + 50);
		
		levelOneMenuItem.setPosition(100, 100);
		
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	}	
}