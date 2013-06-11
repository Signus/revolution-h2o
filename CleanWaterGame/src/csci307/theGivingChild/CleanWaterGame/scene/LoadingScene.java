/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is what you see when the levels loadings
*
* History:
*    5/31/13 original
*/
package csci307.theGivingChild.CleanWaterGame.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;


public class LoadingScene extends BaseScene
{
	@Override
	public void createScene()
	{
		setBackground(new Background(Color.WHITE));
		attachChild(new Text(400, 240, resourcesManager.font, "Loading...", vbom));
	}

	@Override
	public void onBackKeyPressed()
	{
		return;
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene()
	{

	}
}
