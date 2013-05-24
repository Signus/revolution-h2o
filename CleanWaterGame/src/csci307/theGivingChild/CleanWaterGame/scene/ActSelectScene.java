package csci307.theGivingChild.CleanWaterGame.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import android.opengl.GLES20;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;

public class ActSelectScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menuScene;
	
	private final int MENU_ACT_I = 0;
	private final int MENU_TEST = 1;
	
	@Override
	public void createScene() {
		createBackground();	
		createMenuScene();
			
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_ACT_SELECT;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}
	
	private void createBackground() {
		setBackground(new Background(Color.GREEN));
		attachChild(new Text(400, camera.getHeight() - 40, resourcesManager.font, "Act Selection", vbom));	
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,	float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_ACT_I:
				SceneManager.getInstance().createLevelSelectScene();
				SceneManager.getInstance().loadMenuScene(engine);
				return true;
			case MENU_TEST:
				SceneManager.getInstance().loadGameScene(engine, "act1scene2");
				return true;
			default:
				return false;
		}
	}
	
	private MenuScene createMenuScene() {
		menuScene = new MenuScene(camera);
		menuScene.setPosition(0, 0);

//		final IMenuItem resetMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_ACT_I, resourcesManager.font, "Act I", vbom), new Color(1,0,0), new Color(0,0,0));
//		resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//		menuScene.addMenuItem(resetMenuItem);
//
//		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_TEST, resourcesManager.font, "TEst", vbom), new Color(1,0,0), new Color(0,0,0));
//		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//		menuScene.addMenuItem(quitMenuItem);
		
		final IMenuItem actOneItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ACT_I, resourcesManager.act_one_TR, vbom), 1.2f, 1);
		
		menuScene.addMenuItem(actOneItem);
		
		menuScene.buildAnimations();
		menuScene.setBackgroundEnabled(false);
		
		actOneItem.setPosition(400, 200);

		menuScene.setOnMenuItemClickListener(this);
		setChildScene(menuScene);
		return menuScene;
	}

}
