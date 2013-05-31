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

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;
import csci307.theGivingChild.CleanWaterGame.objects.Player;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IOnMenuItemClickListener {

    private static final double TAP_THRESHOLD = 75;
    private static final double SWIPE_THRESHOLD = 100;
    private HUD gameHUD;
	private PhysicsWorld physicsWorld;
	
	private float lastX;
    private float lastY;
    
    private String currentLevel;
	
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HILL = "hill";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND = "ground";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_FLOATINGPLATFORM = "floatingPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_FALLINGPLATFORM = "fallingPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE = "collectable";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_GOAL = "goalcollect";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUNDTEST = "ground2";
	
	private final int MENU_RESUME = 0;
	private final int MENU_QUIT = 1;
	private final int MENU_RESTART = 2;
	
	private Player player;
    private boolean actionPerformed = false;
    private boolean paused = false;

    public GameScene(String level) {
    	this.resourcesManager = ResourceManager.getInstance();
    	this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        currentLevel = level;
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
        this.resourcesManager.backgroundMusic.play();
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
        camera.setBounds(0, 0, 800, 480);
        this.resourcesManager.backgroundMusic.stop();
    }
    
    private void createBackground() {
    	setBackground(new Background(Color.BLUE));
    }
    
    private void createHUD() {
    	gameHUD = new HUD();
    	
    	final Sprite pauseButton = new Sprite(700, 440, resourcesManager.pause_TR, vbom) {
    		@Override
    		public boolean onAreaTouched(TouchEvent touchEvent, float pX, float pY) {
    			if (touchEvent.isActionUp()) {
    				paused = true;
    				setChildScene(pauseScene(), false, true, true);
    				resourcesManager.backgroundMusic.pause();
    			}
    			return true;
    		};
    	};
    	
    	gameHUD.registerTouchArea(pauseButton);
    	gameHUD.attachChild(pauseButton);
    	camera.setHUD(gameHUD);
    }    
    
    private void createPhysics() {
    	physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
    	physicsWorld.setContactListener(contactListener());
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
				
				final IEntity levelObject;
				final Body body;
				/*
				 * Major refactoring has to be done here once the level has images.
				 * this method will return a level object at the end, rather than what is presented now, where some returns in the if block.
				 * 
				 * As for now, the rectangles printed in the level will be made as such so that the jumping which involves contactlistener will work. 
				 */
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HILL)) {
					levelObject = new Rectangle(x, y, width, height, vbom);
					levelObject.setColor(Color.GREEN);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX).setUserData("test");
					return levelObject;
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND)) {
					levelObject = new Rectangle(x, y, width, height, vbom);
					levelObject.setColor(Color.RED);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX).setUserData("test");
					return levelObject;
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUNDTEST)) {
					levelObject = new Sprite(x, y, resourcesManager.ground_TR, vbom);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX).setUserData("test");
					return levelObject;
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FLOATINGPLATFORM)) {
					levelObject = new Rectangle(x, y, width, height, vbom);
					levelObject.setColor(Color.BLACK);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX).setUserData("test");
					return levelObject;
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FALLINGPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.ground_TR, vbom);
					body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX);
					body.setUserData("fallingPlatform");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
					return levelObject;
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
					player = new Player(x, y, vbom, camera, physicsWorld) {
						@Override
						public void onDie() {
							paused = true;
							setChildScene(gameOverScene());
						}
					};
					player.setRunning();
					return player;
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE)) {
					levelObject = new Sprite(x, y, resourcesManager.collectable_TR, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							
							if (player.collidesWith(this)) {
								this.setVisible(false);
								this.setIgnoreUpdate(true);
							}
						}
					};
					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
					
					//level object returned here because it does not need to be registered with the physicsWorld. 
					return levelObject;
					
				} else {
					throw new IllegalArgumentException();
				}
				
				//temporary physics set for every level object. Will need to be put into each entity if physics need to be different. 
//				physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX), true, false));
				
				//disable rendering when not visible. 
//				levelObject.setCullingEnabled(true);
				
//				return levelObject;
			}
			
		});
		
		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".xml");
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.physicsWorld != null) {
            if (player.isNotPerformingAction()) {
                float difX = pSceneTouchEvent.getX() - lastX;
                float difY = pSceneTouchEvent.getY() - lastY;
                double moveDistance =  Math.sqrt(difX * difX + difY * difY);

                switch (pSceneTouchEvent.getAction()) {
                    case TouchEvent.ACTION_DOWN:
                        lastX = pSceneTouchEvent.getX();
                        lastY = pSceneTouchEvent.getY();
                        actionPerformed = false;
                        break;
                    case TouchEvent.ACTION_MOVE:
                        if (!actionPerformed && moveDistance > SWIPE_THRESHOLD) {
                            performPlayerAction(difX, difY, moveDistance);
                            actionPerformed = true;
                        }
                        break;
                    case TouchEvent.ACTION_UP:
                        if (!actionPerformed) {
                            performPlayerAction(difX, difY, moveDistance);
                        }
                        break;
                }
            }
            return true;
        }
		return false;
	}

    private void performPlayerAction(float difX, float difY, double moveDistance) {
        if (difY > 0 && Math.abs(difY) > Math.abs(difX) || moveDistance <= TAP_THRESHOLD) {
            resourcesManager.jumpSound.play();
            player.jump();
        } else if (difX > 0 && difX > Math.abs(difY)) {
            player.dash();
        } else if (difY < 0 && Math.abs(difY) > Math.abs(difX)) {
            player.duck();
        }
    }

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,	float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_RESUME:
				clearChildScene();
				resourcesManager.backgroundMusic.resume();
				paused = false;
				return true;
			case MENU_QUIT:			
				return true;
			case MENU_RESTART:
				resourcesManager.backgroundMusic.stop();
				clearChildScene();
				disposeScene();
				SceneManager.getInstance().loadGameScene(engine, currentLevel);
				paused = false;
				return true;
			default:
				return false;
		}
	}
	
	private MenuScene pauseScene() {
		final MenuScene pauseGame = new MenuScene(camera);
		
		final SpriteMenuItem resumeButton = new SpriteMenuItem(MENU_RESUME, resourcesManager.pause_TR, vbom);
		final Rectangle background = new Rectangle(400, 240, 300, 200, vbom);
		resumeButton.setPosition(400, 240);
		
		//setting background transparent
		background.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		background.setAlpha(0.5f);
		
		pauseGame.attachChild(background);
		pauseGame.addMenuItem(resumeButton);
		
//		pauseGame.setHeight(250);
//		pauseGame.setWidth(360);
		pauseGame.setBackground(new Background(Color.BLACK));

		pauseGame.setBackgroundEnabled(false);
		pauseGame.setOnMenuItemClickListener(this);
		return pauseGame;
	}	
	
	private MenuScene gameOverScene() {
		paused = true;
		final MenuScene gameOver = new MenuScene(camera);
		
		final SpriteMenuItem restartButton = new SpriteMenuItem(MENU_RESTART, resourcesManager.pause_TR, vbom);
		final Rectangle background = new Rectangle(400, 240, 300, 200, vbom);
		
		restartButton.setPosition(400,  240);
		
		gameOver.attachChild(background);
		gameOver.addMenuItem(restartButton);
		gameOver.setBackgroundEnabled(false);
		gameOver.setOnMenuItemClickListener(this);
		return gameOver;
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(paused) {
			return;
		}
	}
	
	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				
				
				
				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
					if (x2.getBody().getUserData().equals("player") || x1.getBody().getUserData().equals("player"))
					{
						player.increaseFootContacts();
					}
					if (x1.getBody().getUserData().equals("fallingPlatform") && x2.getBody().getUserData().equals("player")) {
						engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {
							
							@Override
							public void onTimePassed(TimerHandler pTimerHandler) {
								pTimerHandler.reset();
								engine.unregisterUpdateHandler(pTimerHandler);
								x1.getBody().setType(BodyType.DynamicBody);
								
							}
						}));
					}
				}				
			}

			@Override
			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{
					if (x2.getBody().getUserData().equals("player"))
					{
						player.decreaseFootContacts();
						System.out.println("000");
					}
				}
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
			
		};
		return contactListener;
	}
}