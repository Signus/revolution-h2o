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
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
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

    private static final double TAP_THRESHOLD = 60;
    private static final double SWIPE_THRESHOLD = 80;
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
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_FALLINGPLATFORM_2 = "fallingPlatform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE = "collectable";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE_GOAL = "goalcollect";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUNDTEST = "ground2";

	//Categories of objects
	private static final short CATEGORYBIT_GROUND = 1;
	private static final short CATEGORYBIT_FALLING = 2;
	private static final short CATEGORYBIT_PLAYER = 4;

	//What shoiuld collide with what objects.
	private static final short MASKBITS_GROUND = CATEGORYBIT_GROUND + CATEGORYBIT_PLAYER;
	private static final short MASKBITS_FALLING = CATEGORYBIT_FALLING + CATEGORYBIT_PLAYER;
	private static final short MASKBITS_PLAYER = CATEGORYBIT_FALLING + CATEGORYBIT_GROUND + CATEGORYBIT_PLAYER;

	private static final FixtureDef GROUND_FIX = PhysicsFactory.createFixtureDef(0, 0.01f, 0.1f, false, CATEGORYBIT_GROUND, MASKBITS_GROUND, (short)0);
	private static final FixtureDef FALLING_FIX = PhysicsFactory.createFixtureDef(1, 0, 0.1f, false, CATEGORYBIT_FALLING, MASKBITS_FALLING, (short)0);
	public static final FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(0, 0, 0, false, CATEGORYBIT_PLAYER, MASKBITS_PLAYER, (short)0);

	private final int MENU_RESUME = 0;
	private final int MENU_QUIT = 1;
	private final int MENU_RESTART = 2;
	private final int MENU_OPTIONS = 3;

	private Player player;
    private boolean actionPerformed = false;
    private boolean paused = false;
    private boolean isDone = false;

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
        //this.resourcesManager.backgroundMusic.play();
    }

    @Override
    public void onBackKeyPressed()
    {
    	if (hasChildScene()) {
    		clearChildScene();
    		paused = false;
    	} else {
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
    }

    private void createBackground() {
//    	setBackground(new Background(Color.BLUE));
    	AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(.5f*camera.getWidth() , .5f*camera.getHeight(), resourcesManager.scene_background_TR, vbom)));
		setBackground(autoParallaxBackground);
    }

    private void createHUD() {
    	gameHUD = new HUD();

    	final Sprite pauseButton = new Sprite(50, 430, resourcesManager.pause_TR, vbom) {
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
//				final Body body;
				/*
				 * Major refactoring has to be done here once the level has images.
				 * this method will return a level object at the end, rather than what is presented now, where some returns in the if block.
				 *
				 * As for now, the rectangles printed in the level will be made as such so that the jumping which involves contactlistener will work.
				 */
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_HILL)) {
					levelObject = new Rectangle(x, y, width, height, vbom);
					levelObject.setColor(Color.GREEN);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, GROUND_FIX).setUserData("hill");
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND)) {
					levelObject = new Sprite(x, y, resourcesManager.ground_TR, vbom);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, GROUND_FIX).setUserData("ground");
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUNDTEST)) {
					levelObject = new Sprite(x, y, resourcesManager.ground_TR, vbom);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, GROUND_FIX).setUserData("test");
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FLOATINGPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.floating_platform_ground_TR, vbom);
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
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FALLING_FIX);
					body.setUserData("fallingPlatform");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_FALLINGPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.ground_TR, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FALLING_FIX);
					body.setUserData("fallingPlatform");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
					player = new Player(x, y, vbom, camera, physicsWorld) {
						@Override
						public void onDie() {
                            isDone = true;
							paused = true;
							camera.setChaseEntity(null);
							setChildScene(gameOverScene());
						}
					};
					player.setRunning();
					levelObject = player;
				}
				else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_ITEM_COLLECTABLE)) {
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

					//the coin will animate.
					levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));

					//level object returned here because it does not need to be registered with the physicsWorld.
					return levelObject;

				} else {
					throw new IllegalArgumentException();
				}

				//temporary physics set for every level object. Will need to be put into each entity if physics need to be different.
//				physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX), true, false));

				//disable rendering when not visible.
				levelObject.setCullingEnabled(true);
				return levelObject;
			}

		});

		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".xml");
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (this.physicsWorld != null) {
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
		return false;
	}

    private void performPlayerAction(float difX, float difY, double moveDistance) {
        if (difY > 0 && Math.abs(difY) > Math.abs(difX) || moveDistance <= TAP_THRESHOLD) {
            resourcesManager.jumpSound.play();
            player.jump();
        } else if (difX > 0 && difX > Math.abs(difY)) {
        	resourcesManager.dashSound.play();
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
				clearChildScene();
				SceneManager.getInstance().loadMenuScene(engine);
				return true;
			case MENU_RESTART:
				resourcesManager.backgroundMusic.stop();
				clearChildScene();
				disposeScene();
				SceneManager.getInstance().loadGameScene(engine, currentLevel);
				paused = false;
                isDone = false;
				return true;
			case MENU_OPTIONS:
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
		final IMenuItem optionsMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_OPTIONS, resourcesManager.font, "OPTIONS", vbom), Color.RED, Color.WHITE);
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

//		gameOver.attachChild(new Text(400, 400, resourcesManager.game_font, "GAME OVER", vbom));

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
					if (contactObject(x1, x2, "ground") || contactObject(x1, x2, "hill") || contactObject(x1, x2, "test") || contactObject(x1, x2, "fallingPlatform"))
					{
						player.setContactGround();
					}
					if (x1.getBody().getUserData().equals("fallingPlatform") && x2.getBody().getUserData().equals("player")) {
//						System.out.println("x1 " + x1.getBody().getPosition().x);
//						System.out.println("x2 " + x2.getBody().getPosition().x);
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
					} else if (x1.getBody().getUserData().equals("player") && x2.getBody().getUserData().equals("fallingPlatform")) {
//						System.out.println("distance body: " + x1.getBody().getPosition().dst(x2.getBody().getPosition()));
//						System.out.println("x1 " + x1.getBody().getPosition().x);
//						System.out.println("x2 " + x2.getBody().getPosition().x);
						if (x2.getBody().getPosition().y < x1.getBody().getPosition().y) {
							engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {

								@Override
								public void onTimePassed(TimerHandler pTimerHandler) {
									pTimerHandler.reset();
									engine.unregisterUpdateHandler(pTimerHandler);
									x2.getBody().setType(BodyType.DynamicBody);
							//		x2.getBody().setLinearVelocity(0, -17);

								}
							}));
						}
					}
				}
			}

            private boolean contactObject(Fixture x1, Fixture x2, String object) {
                return x2.getBody().getUserData().equals("player") && x1.getBody().getUserData().equals(object) || x2.getBody().getUserData().equals(object) && x1.getBody().getUserData().equals("player");
            }

			@Override
			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
				{
					if ((x2.getBody().getUserData().equals("player")) || (x1.getBody().getUserData().equals("player")))
					{
						player.unsetContactGround();
//						System.out.println("ENDGROUND");
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