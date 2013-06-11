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
import java.util.ArrayList;
import java.util.Iterator;

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
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
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

import csci307.theGivingChild.CleanWaterGame.CleanWaterGame;
import csci307.theGivingChild.CleanWaterGame.GameLauncher;
import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;
import csci307.theGivingChild.CleanWaterGame.objects.FallingPlatform;
import csci307.theGivingChild.CleanWaterGame.objects.Player;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IOnMenuItemClickListener {

    private static final double TAP_THRESHOLD = 60;
    private static final double SWIPE_THRESHOLD = 80;
    private static final double COLLISION_THRESHOLD = 1.0;
    private HUD gameHUD;
	private PhysicsWorld physicsWorld;

	private float lastX;
    private float lastY;

    private String currentLevel;
    private int level;
    private boolean start = false;

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
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_FALLINGPLATFORM_2 = "fallingPlatform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE = "collectable";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_WIN_TRIGGER = "winTrigger";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE2_GOALS = "twine";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE3_GOALS = "stone";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE4_GOALS = "mud";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE5_GOALS = "wood";

	//Categories of objects
	private static final short CATEGORYBIT_GROUND = 1;
	private static final short CATEGORYBIT_FALLING = 2;
	private static final short CATEGORYBIT_PLAYER = 4;
	private static final short CATEGORYBIT_FALLING_2 = 8;

	//What shoiuld collide with what objects.
	private static final short MASKBITS_GROUND = CATEGORYBIT_GROUND + CATEGORYBIT_PLAYER;
	private static final short MASKBITS_FALLING = CATEGORYBIT_FALLING + CATEGORYBIT_PLAYER;
	private static final short MASKBITS_PLAYER = CATEGORYBIT_FALLING + CATEGORYBIT_GROUND;
	private static final short MASKBITS_FALLING_2 = CATEGORYBIT_PLAYER;

	private static final FixtureDef GROUND_FIX = PhysicsFactory.createFixtureDef(0, 0.01f, 0.1f, false, CATEGORYBIT_GROUND, MASKBITS_GROUND, (short)0);
	public static final FixtureDef FALLING_FIX = PhysicsFactory.createFixtureDef(1, 0, 0.1f, false, CATEGORYBIT_FALLING, MASKBITS_FALLING, (short)0);
	private static final FixtureDef FALLING_FIX_2 = PhysicsFactory.createFixtureDef(1, 0, 0.1f, false, CATEGORYBIT_FALLING, MASKBITS_FALLING_2, (short)0);
	public static final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_PLAYER, MASKBITS_PLAYER, (short)0);

	private final int MENU_RESUME = 0;
	private final int MENU_QUIT = 1;
	private final int MENU_RESTART = 2;
	private final int MENU_OPTIONS = 3;
	private final int MENU_NEXT = 4;
	
	private Sprite heart1;
	private Sprite heart2;
	private Sprite heart3;

	private Player player;
    private boolean actionPerformed = false;
    public static boolean paused = false;
    private boolean isDone = false;
    private ArrayList<IEntity> levelObjects = new ArrayList<IEntity>();
    
    public static PausedType pausedType = PausedType.PAUSED_OFF;
    
    public enum PausedType {
    	PAUSED_OFF,
    	PAUSED_ON,
    	PAUSED_GAMEOVER,
    	PAUSED_GAMEWIN
    }
    
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
        if (!ResourceManager.getInstance().isMuted())
        {
        	this.resourcesManager.backgroundMusic.play();
        }
    }

    @Override
    public void onBackKeyPressed()
    {
    	if (hasChildScene()) {
    		if(!ResourceManager.getInstance().isMuted()) ResourceManager.getInstance().backgroundMusic.play();
    		//if(!ResourceManager.getInstance().isMuted()) CleanWaterGame.getInstance().playMenuMusic();
    		//CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false).commit();
    		clearChildScene();
    		paused = false;
    		pausedType = PausedType.PAUSED_OFF;
    	} else {
    		if(!ResourceManager.getInstance().isMuted()) CleanWaterGame.getInstance().playMenuMusic();
    		CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false).commit();
    		SceneManager.getInstance().loadMenuScene(engine);
    	}

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
        
        Iterator<Body> allBodies = this.physicsWorld.getBodies();
        while (allBodies.hasNext()) {
        	try {
        		final Body currentBody = allBodies.next();
        		physicsWorld.destroyBody(currentBody);
        	} catch (Exception e) {
        		Debug.e(e);
        	}
        }
        
        if (levelObjects.size() > 0) {
        	engine.runOnUpdateThread(new Runnable() {
        		@Override
        		public void run() {
        			for (IEntity object : levelObjects) {
        				try {
        					detachChild(object);
        				} catch (Exception e) {
        					Debug.e(e);
        				}
                	}
        		}
        	});
        	
        }
        levelObjects.clear();
        this.clearChildScene();
        this.reset();
        this.detachSelf();
        physicsWorld.clearForces();
        physicsWorld.reset();
    }

    private void createBackground() {
//    	setBackground(new Background(Color.BLUE));
    	AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(.5f*camera.getWidth() , .5f*camera.getHeight(), resourcesManager.scene_background_TR, vbom)));
		Sprite cloudSprite = new Sprite(.5f*camera.getWidth(), .5f*camera.getHeight()+60f, resourcesManager.scene_foreground_TR, vbom);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5f, cloudSprite ));
		setBackground(autoParallaxBackground);
    }

    private void createHUD() {
    	gameHUD = new HUD();
    	heart1 = new Sprite(120, 420, resourcesManager.hitpoints_TR, vbom);
    	heart2 = new Sprite(170, 420, resourcesManager.hitpoints_TR, vbom);
    	heart3 = new Sprite(220, 420, resourcesManager.hitpoints_TR, vbom);
    	

    	final Sprite pauseButton = new Sprite(50, 430, resourcesManager.pause_TR, vbom) {
    		@Override
    		public boolean onAreaTouched(TouchEvent touchEvent, float pX, float pY) {
    			if (touchEvent.isActionUp()) {
    				paused = true;
    				pausedType = PausedType.PAUSED_ON;
//    				setChildScene(pauseScene(), false, true, true);
    				resourcesManager.backgroundMusic.pause();
    			}
    			return true;
    		};
    	};

    	gameHUD.registerTouchArea(pauseButton);
    	gameHUD.attachChild(pauseButton);
    	gameHUD.attachChild(heart1);
    	gameHUD.attachChild(heart2);
    	gameHUD.attachChild(heart3);
    	displayHealth(3);
    	
    	camera.setHUD(gameHUD);
    }
    
    private void displayHealth(int hitpoints) {
    	switch (hitpoints) {
	    	case 0:
				heart1.setVisible(false);
				heart2.setVisible(false);
				heart3.setVisible(false);
				break;
    		case 1:
    			heart1.setVisible(true);
    			heart2.setVisible(false);
    			heart3.setVisible(false);
    			break;
    		case 2:
    			heart1.setVisible(true);
    			heart2.setVisible(true);
    			heart3.setVisible(false);
    			break;
    		case 3:
    			heart1.setVisible(true);
    			heart2.setVisible(true);
    			heart3.setVisible(true);
    			break;	
    	}
    }

    private void createPhysics() {
    	physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
    	physicsWorld.setContactListener(contactListener());
    	registerUpdateHandler(physicsWorld);
    }

    private void loadLevel(String levelID) {
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

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
				
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HILL)) {
					levelObject = new Sprite(x, y, resourcesManager.hill_TR, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							//side collision
							if(detectSideCollision(player, this)) {
								player.bounceBack();
								player.decrementHP();
								displayHealth(player.getHP());
							}
						}
					};
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, GROUND_FIX).setUserData("ground");
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND)) {
					levelObject = new Sprite(x, y, resourcesManager.ground_TR, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							//side collision
							if(detectSideCollision(player, this)) {
								player.bounceBack();
								player.decrementHP();
								displayHealth(player.getHP());
							}
						}
					};
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, GROUND_FIX).setUserData("ground");
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FLOATINGPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.floating_platform_ground_TR, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							
							if(detectSideCollision(player, this)) {
								player.bounceBack();
								player.decrementHP();
								displayHealth(player.getHP());
							}
						}
					};
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, GROUND_FIX).setUserData("test");
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FALLINGPLATFORM_2)) {
					levelObject = new Sprite(x, y, resourcesManager.falling_platform_2_TR, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);

							if (this.getY() < 0) {
								detachChild(this);
							}
						}
					};
					body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FALLING_FIX);
					body.setUserData("fallingPlatform2");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FALLINGPLATFORM)) {
					
					levelObject = new FallingPlatform(x, y, vbom, camera, physicsWorld, resourcesManager.falling_platform_TR, engine) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							
							if (detectTopCollision(player, this)) {
								platformFall();
							}
							if (detectSideCollision(player, this)) {
								player.bounceBack();
								player.decrementHP();
								displayHealth(player.getHP());
							}
						}
					};
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
					player = new Player(x, y, vbom, camera, physicsWorld, 3, resourcesManager.player_TR) {
												
						@Override
						public void onDie() {
                            isDone = true;
							pausedType = PausedType.PAUSED_GAMEOVER;
							camera.setChaseEntity(null);
						}
						
					};					
					levelObject = player;
				} 
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_WIN_TRIGGER)) {
					levelObject = new Rectangle(x, y, width, height, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							if (player.collidesWith(this)) {
								this.setIgnoreUpdate(true);
								//show level complete scene.
								System.out.println("TRIGGER WORKS");
								
								setChildScene(gameWinScene());
								pausedType = PausedType.PAUSED_GAMEWIN;
							}
						}
					};
					levelObject.setVisible(false);
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE)) {					
					levelObject = loadCollectable(x, y, resourcesManager.collectable_TR, 20);
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE2_GOALS)) {
					levelObject = loadCollectable(x, y, resourcesManager.twine_TR, 20);
				} 
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE3_GOALS)) {
					levelObject = loadCollectable(x, y, resourcesManager.wood_TR, 20);
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE4_GOALS)) {
					levelObject = loadCollectable(x, y, resourcesManager.stone_TR, 20);
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_ACT1_SCENE5_GOALS)) {
					levelObject = loadCollectable(x, y, resourcesManager.collectable_TR, 20);
				} else {
					throw new IllegalArgumentException();
				}

				//disable rendering when not visible.
				levelObject.setCullingEnabled(true);
				levelObjects.add(levelObject);
				return levelObject;
			}

		});

		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".xml");
	}
    
    private Sprite loadCollectable(float x, float y, ITextureRegion region, int score) {
    	Sprite sprite = new Sprite(x, y, region, vbom) {
    		@Override
    		protected void onManagedUpdate(float pSecondsElapsed) {
				super.onManagedUpdate(pSecondsElapsed);

				if (player.collidesWith(this)) {
					this.setVisible(false);
					this.setIgnoreUpdate(true);
				}
			}
    	};   
    	    	
    	sprite.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
    	return sprite;
    }
    
    private boolean detectSideCollision(Player player, IEntity object) {
    	if ( ((player.getX() + player.getWidth()/2.0) + COLLISION_THRESHOLD) > (object.getX() - object.getWidth() / 2.0) &&
				(player.getX() + player.getWidth()/2.0) < (object.getX() + object.getWidth() / 2.0) &&
				((player.getY() - player.getHeight()/2.0) + COLLISION_THRESHOLD) < (object.getY() + object.getHeight()/2.0) && 
				((player.getY() + player.getHeight()/2.0) - COLLISION_THRESHOLD) > (object.getY() - object.getHeight() / 2.0) ) {
			System.out.println("SIDE COLLISION");
			System.out.println("x positions: " + ((player.getX() + player.getWidth()/2.0) + COLLISION_THRESHOLD) + "  > " + (object.getX() - object.getWidth() / 2.0));
			System.out.println(((player.getX() + player.getWidth()/2.0)) + "  < " + (object.getX() + object.getWidth() / 2.0));
			System.out.println(((player.getY() - player.getHeight()/2.0) - COLLISION_THRESHOLD) + "   < " + (object.getY() + object.getHeight()/2.0));
			System.out.println(((player.getY() + player.getHeight()/2.0) + COLLISION_THRESHOLD) + "   > " + ((object.getY() - object.getHeight() / 2.0)));
			
			return true;
		}
    	return false;
    }
    
    private boolean detectTopCollision(Player player, IEntity object) {
    	if ( ((player.getX() + player.getWidth()/2.0) + COLLISION_THRESHOLD) > (object.getX() - object.getWidth() / 2.0) &&
				(player.getX() + player.getWidth()/2.0) < (object.getX() + object.getWidth() / 2.0) &&
				(player.getY() - player.getHeight()/2.0) < (object.getY() + object.getHeight()/2.0 + COLLISION_THRESHOLD) &&
				(player.getY() - player.getHeight()/2.0) > (object.getY() + object.getHeight()/2.0 - COLLISION_THRESHOLD)) {
    		return true;
    	}
    	return false;
    }

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.physicsWorld != null) {
			if (!start) {
				if (pSceneTouchEvent.isActionUp()) {
					player.setRunning();
                	start = true;
                	return true;
				}
			} else {
            if (player.isNotPerformingAction() && !isDone) {
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
                            actionPerformed = true;
                        }
                        break;
                }
            }
            return true;
			}
        }
		return false;
	}

    private void performPlayerAction(float difX, float difY, double moveDistance) {
        if (difY > 0 && Math.abs(difY) > Math.abs(difX) || moveDistance <= TAP_THRESHOLD) {
        	
            player.jump();
            
        } else if (difX > 0 && difX > Math.abs(difY)) {
            player.dash();
        }
    }

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,	float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_RESUME:
				clearChildScene();
				if(!ResourceManager.getInstance().isMuted()) resourcesManager.backgroundMusic.resume();
				pausedType = PausedType.PAUSED_OFF;
				return true;
			case MENU_QUIT:
				clearChildScene();
				SceneManager.getInstance().loadMenuScene(engine);
				if(!ResourceManager.getInstance().isMuted()) CleanWaterGame.getInstance().playMenuMusic();
				CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY_INGAME, ResourceManager.getInstance().activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_INGAME_MUTE, false).commit();
				pausedType = PausedType.PAUSED_OFF;
				return true;
			case MENU_RESTART:
				resourcesManager.backgroundMusic.stop();
				clearChildScene();
				disposeScene();
				SceneManager.getInstance().loadGameScene(engine, currentLevel);
				pausedType = PausedType.PAUSED_OFF;
                isDone = false;
				return true;
			case MENU_OPTIONS:
				ResourceManager.getInstance().toggleMute();
				if(ResourceManager.getInstance().isMuted())
				{
					resourcesManager.backgroundMusic.pause();
				}
				return true;
			case MENU_NEXT:
				return true;
			default:
				return false;
		}
	}

	private MenuScene pauseScene() {
		final MenuScene pauseGame = new MenuScene(camera);

		final IMenuItem resumeMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESUME, resourcesManager.font, "RESUME", vbom), Color.RED, Color.WHITE);
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, resourcesManager.font, "QUIT", vbom), Color.RED, Color.WHITE);
		final IMenuItem restartMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESTART, resourcesManager.font, "RESTART", vbom), Color.RED, Color.WHITE);
		final IMenuItem optionsMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_OPTIONS, resourcesManager.font, "MUTE/UNMUTE", vbom), Color.RED, Color.WHITE);
		final Rectangle background = new Rectangle(400, 240, 300, 200, vbom);

		int menuPositionDifference = (int) (background.getHeight() / 5);
		resumeMenuItem.setPosition(400, menuPositionDifference * 4 + 140);
		restartMenuItem.setPosition(400, menuPositionDifference * 3 + 140);
		optionsMenuItem.setPosition(400, menuPositionDifference * 2 + 140);
		quitMenuItem.setPosition(400, menuPositionDifference + 140);


		//setting background transparent
		background.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		background.setAlpha(0.5f);

		pauseGame.attachChild(background);
		pauseGame.addMenuItem(resumeMenuItem);
		pauseGame.addMenuItem(quitMenuItem);
		pauseGame.addMenuItem(restartMenuItem);
		pauseGame.addMenuItem(optionsMenuItem);

		pauseGame.setBackgroundEnabled(false);
		pauseGame.setOnMenuItemClickListener(this);

		return pauseGame;
	}

	private MenuScene gameOverScene() {
		final MenuScene gameOver = new MenuScene(camera);

		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, resourcesManager.font, "QUIT", vbom), Color.RED, Color.WHITE);
		final IMenuItem restartMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESTART, resourcesManager.font, "RESTART", vbom), Color.RED, Color.WHITE);
		final Rectangle background = new Rectangle(400, 240, 300, 200, vbom);

		int menuPositionDifference = (int) (background.getHeight() / 3);
		restartMenuItem.setPosition(400, menuPositionDifference * 2 + 140);
		quitMenuItem.setPosition(400, menuPositionDifference + 140);

		gameOver.attachChild(new Text(400, 400, resourcesManager.font, "GAME OVER", vbom));

		//setting background transparent
		background.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		background.setAlpha(0.5f);

		gameOver.attachChild(background);
		gameOver.addMenuItem(restartMenuItem);
		gameOver.addMenuItem(quitMenuItem);
		gameOver.setBackgroundEnabled(false);
		gameOver.setOnMenuItemClickListener(this);
		return gameOver;
	}
	
	private MenuScene gameWinScene() {
		final MenuScene gameWin = new MenuScene(camera);
		
		final IMenuItem nextMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_NEXT, resourcesManager.font, "NEXT LEVEL", vbom), Color.RED, Color.WHITE);
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, resourcesManager.font, "QUIT", vbom), Color.RED, Color.WHITE);
		final IMenuItem restartMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESTART, resourcesManager.font, "REPLAY", vbom), Color.RED, Color.WHITE);
		final Rectangle background = new Rectangle(400, 240, 300, 200, vbom);
		
		int menuPositionDifference = (int) (background.getHeight() / 4);
		nextMenuItem.setPosition(400, menuPositionDifference * 3 + 140 );
		restartMenuItem.setPosition(400, menuPositionDifference * 2 + 140);
		quitMenuItem.setPosition(400, menuPositionDifference + 140);
		
		//setting background transparent
		background.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		background.setAlpha(0.5f);
		
		gameWin.attachChild(background);
		gameWin.addMenuItem(nextMenuItem);
		gameWin.addMenuItem(restartMenuItem);
		gameWin.addMenuItem(quitMenuItem);
		gameWin.setBackgroundEnabled(false);
		gameWin.setOnMenuItemClickListener(this);
		
		return gameWin;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		switch (pausedType) {
			case PAUSED_OFF:
				super.onManagedUpdate(pSecondsElapsed);
				break;
			case PAUSED_GAMEOVER:
				setChildScene(gameOverScene());
				return;
			case PAUSED_ON:
				setChildScene(pauseScene());
				return;
			case PAUSED_GAMEWIN:
//				setChildScene(gameWinScene());
				return;
			default:
				super.onManagedUpdate(pSecondsElapsed);
		}				
	}

	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();


				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) {
					if (x1.getBody().getUserData().equals("fallingPlatform2") && x2.getBody().getUserData().equals("player")) {
						if (x1.getBody().getPosition().y < x2.getBody().getPosition().y &&
								x1.getBody().getPosition().x > x2.getBody().getPosition().x) {
							engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {

								@Override
								public void onTimePassed(TimerHandler pTimerHandler) {
									pTimerHandler.reset();
									engine.unregisterUpdateHandler(pTimerHandler);
									x1.getBody().setType(BodyType.DynamicBody);

								}
							}));
						}
					} else if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("fallingPlatform2")) {
						if (x2.getBody().getPosition().y < x1.getBody().getPosition().y) {
							engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {

								@Override
								public void onTimePassed(TimerHandler pTimerHandler) {
									pTimerHandler.reset();
									engine.unregisterUpdateHandler(pTimerHandler);
									x2.getBody().setType(BodyType.DynamicBody);
								}
							}));
						}
					}
				}
			}

			@Override
			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{

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