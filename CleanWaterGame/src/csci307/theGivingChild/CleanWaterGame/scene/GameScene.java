package csci307.theGivingChild.CleanWaterGame.scene;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.math.Vector2;


import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;

public class GameScene extends BaseScene {
	
	private HUD gameHUD;
	private PhysicsWorld physicsWorld;
	
    @Override
    public void createScene()
    {
        createBackground();
        createHUD();
        createPhysics();
    }

    @Override
    public void onBackKeyPressed()
    {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType()
    {
        return SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene()
    {
        camera.setHUD(null);
        camera.setCenter(400, 240);
    }
    
    private void createBackground() {
    	setBackground(new Background(Color.GREEN));
    }
    
    private void createHUD() {
    	gameHUD = new HUD();
    	camera.setHUD(gameHUD);
    }    
    
    private void createPhysics() {
    	physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
    	registerUpdateHandler(physicsWorld);
    }
}