/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is makes the menu for act selection from and engine
*
* History:
*    5/31/13 original
*/
package csci307.theGivingChild.CleanWaterGame.scene;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;

public class ActSelectScene extends BaseScene implements IOnMenuItemClickListener {

	private MenuScene menuScene;
	private MenuScene messageScene;
	
	private final int MENU_ACT_I = 0;
	private final int MENU_LOCKED = 1;
	private final int MENU_RESUME = 2;
	
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
		
	}
	
	private void createBackground() {
		AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(.5f*camera.getWidth() , .5f*camera.getHeight(), resourcesManager.act_menu_background_TR, vbom)));
		setBackground(autoParallaxBackground);
		attachChild(new Text(400, camera.getHeight() - 40, resourcesManager.font, "Act Selection", vbom));	
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,	float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_ACT_I:
				SceneManager.getInstance().createLevelSelectScene();
				SceneManager.getInstance().loadMenuScene(engine);
				return true;
			case MENU_LOCKED:
				clearChildScene();
				setChildScene(createMessageScene());
				return true;
			case MENU_RESUME:
				clearChildScene();
				createMenuScene();
				return true;
			default:
				return false;
		}
	}
	
	/*
	 * Shows the act buttons. 
	 */
	private MenuScene createMenuScene() {
		menuScene = new MenuScene(camera);
		menuScene.setPosition(0, 0);
		
		final IMenuItem actOneItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_ACT_I, resourcesManager.act_one_TR, vbom), 1.2f, 1);
		final IMenuItem actTwoItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LOCKED, resourcesManager.act_two_TR, vbom), 1.2f, 1);
		final IMenuItem actThreeItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LOCKED, resourcesManager.act_three_TR, vbom), 1.2f, 1);
		final IMenuItem actFourItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_LOCKED, resourcesManager.act_four_TR, vbom), 1.2f, 1);
		
		menuScene.addMenuItem(actOneItem);
		menuScene.addMenuItem(actTwoItem);
		menuScene.addMenuItem(actThreeItem);
		menuScene.addMenuItem(actFourItem);
		
		menuScene.buildAnimations();
		menuScene.setBackgroundEnabled(false);
		
		actOneItem.setPosition(150, 200);
        actTwoItem.setPosition(300, 200);
        actThreeItem.setPosition(450, 200);
        actFourItem.setPosition(600,200);

		menuScene.setOnMenuItemClickListener(this);
		setChildScene(menuScene);
		return menuScene;
	}
	
	/*
	 * Creates the message scene to show that the act is locked. 
	 */
	private MenuScene createMessageScene() {
		messageScene = new MenuScene(camera);
		
		final Rectangle background = new Rectangle(400, 240, 600, 200, vbom);
		messageScene.attachChild(background);
		final IMenuItem resumeMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESUME, resourcesManager.font, ">>", vbom), Color.RED, Color.WHITE);
		resumeMenuItem.setPosition(650, 150);
		messageScene.attachChild(new Text(400, 300, resourcesManager.font, "ACT LOCKED IN THE", vbom));
		messageScene.attachChild(new Text(400, 250, resourcesManager.font, "LITE VERSION", vbom));
		
		background.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		background.setAlpha(0.7f);
		
		messageScene.addMenuItem(resumeMenuItem);
		messageScene.setBackgroundEnabled(false);
		messageScene.setOnMenuItemClickListener(this);
		
		return messageScene;
	}
}
