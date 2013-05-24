/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/22/13
 * Description: Running Game Scene
 * 
 * TODO --------
 * -HUD
 * -collision (contact listener)
 * -game over stuff
 */

package csci307.theGivingChild.CleanWaterGame.scene;

import java.io.IOException;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;
import csci307.theGivingChild.CleanWaterGame.objects.Player;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private static final double TAP_THRESHOLD = 30;
    private HUD gameHUD;
	private PhysicsWorld physicsWorld;
	
	private float lastX;
    private float lastY;
	
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HILL = "hill";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND = "ground";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_FLOATINGPLATFORM = "floatingPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	
	private Player player;	
	
    public GameScene(String level) {
    	this.resourcesManager = ResourceManager.getInstance();
    	this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene();
		loadLevel(level);
	}

	@Override
    public void createScene()
    {
        createBackground();
        createHUD();
        createPhysics();
        
        setOnSceneTouchListener(this); 
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
    	setBackground(new Background(Color.BLUE));
    }
    
    private void createHUD() {
    	gameHUD = new HUD();
    	camera.setHUD(gameHUD);
    }    
    
    private void createPhysics() {
    	physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
    	registerUpdateHandler(physicsWorld);
    }
    
    private void loadLevel(String levelID) {
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
		
		final FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0, 0.01f, 0.1f);
		
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {

			@Override
			public IEntity onLoadEntity(String pEntityName, IEntity pParent, Attributes pAttributes, SimpleLevelEntityLoaderData pEntityLoaderData)	throws IOException {
				final int level_width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int level_height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				
				camera.setBounds(0, 0, level_width, level_height);
				camera.setBoundsEnabled(true);
				
				return GameScene.this;
			}			
		});
		
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {

			@Override
			public IEntity onLoadEntity(String pEntityName, IEntity pParent, Attributes pAttributes, SimpleLevelEntityLoaderData pEntityLoaderData)	throws IOException {
				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_HEIGHT);
				final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
				
				final Rectangle levelObject;
				
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HILL)) {
					levelObject = new Rectangle(x, y, width, height, vbom);
					levelObject.setColor(Color.GREEN);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND)) {
					levelObject = new Rectangle(x, y, width, height, vbom);
					levelObject.setColor(Color.RED);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FLOATINGPLATFORM)){
					levelObject = new Rectangle(x, y, width, height, vbom);
					levelObject.setColor(Color.BLACK);
				}else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
					player = new Player(x, y, vbom, camera, physicsWorld) {
						@Override
						public void onDie() {
							//do something, like show game over
						}
					};
					player.setRunning();
					return player;
				} else {
					throw new IllegalArgumentException();
				}
				
				physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX), true, false));
				
				return levelObject;
			}
			
		});
		
		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".xml");
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.physicsWorld != null) {
            switch (pSceneTouchEvent.getAction()) {
                case TouchEvent.ACTION_DOWN:
                    lastX = pSceneTouchEvent.getX();
                    lastY = pSceneTouchEvent.getY();
                    break;
                case TouchEvent.ACTION_MOVE:
                    break;
                case TouchEvent.ACTION_UP:
                    float difX = pSceneTouchEvent.getX() - lastX;
                    float difY = pSceneTouchEvent.getY() - lastY;

                    if (difX > 0 && difX > Math.abs(difY)) {
                        // do something with image and velocity (sprint)

                    }
                    if (difY < 0 && Math.abs(difY) > Math.abs(difX)) {
                        // do something with image (slide)
                    }
                    if (difY > 0 && Math.abs(difY) > Math.abs(difX) || Math.sqrt(difX * difX + difY * difY) <= TAP_THRESHOLD) {
                        // do something with image (jump)
                    //	this.jumpSound.play();
                        player.jump();
                    }

                    break;
            }
            return true;
        }
		return false;
	}
    
    
}