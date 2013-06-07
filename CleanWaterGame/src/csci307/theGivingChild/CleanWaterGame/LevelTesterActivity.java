package csci307.theGivingChild.CleanWaterGame;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
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
import csci307.theGivingChild.CleanWaterGame.objects.Player;


public class LevelTesterActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener {

	private static final int CAMERA_WIDTH = 1200;
	private static final int CAMERA_HEIGHT = 800;
	
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_WIDTH = "width";
	private static final String TAG_ENTITY_ATTRIBUTE_HEIGHT = "height";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_OBSTACLE1 = "obstacle1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND1 = "ground1";
    private static final double TAP_THRESHOLD = 30;
    private static final double SWIPE_THRESHOLD = 80;

    private BitmapTextureAtlas spriteAtlas;
	private TiledTextureRegion nyanRegion;
	
	private BitmapTextureAtlas bgAtlas;
	private ITextureRegion backGroundRegion;
	private ITextureRegion midGroundRegion;
	private ITextureRegion frontGroundRegion;
	
	private static Sound jumpSound;
	private static Music backgroundMusic;
	
	private AnimatedSprite background;
	
	private PhysicsWorld physicsWorld;
	final static FixtureDef PLAYER_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
	final static FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.0f);
	
	
	private Scene scene;
	//private Body playerBody;
	private BoundCamera camera;
    private float lastX;
    private float lastY;
    private Player player;
	
	private boolean actionPerformed = false;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions;
		engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
	    engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		spriteAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1024, 128, TextureOptions.DEFAULT);
		nyanRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(spriteAtlas, this, "player_run_sprite.png", 0, 0, 6, 1);
		spriteAtlas.load();
		
		bgAtlas = new BitmapTextureAtlas(this.getTextureManager(), 2048, 2048, TextureOptions.DEFAULT);
		backGroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgAtlas, this, "background.png", 0, 0);
		midGroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgAtlas, this, "clouds.png", 0, 800);
		frontGroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgAtlas, this, "ground.png", 0, 1179);
		
		bgAtlas.load();
		
		//SOUND
		SoundFactory.setAssetBasePath("sfx/");
		try {
			this.jumpSound = SoundFactory.createSoundFromAsset(getSoundManager(),this, "jump.mp3");
			this.jumpSound.setVolume(.5f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//MUSIC
		MusicFactory.setAssetBasePath("sfx/");
		try {
			this.backgroundMusic = MusicFactory.createMusicFromAsset(getMusicManager(),this, "jungle.mp3");
			this.backgroundMusic.setLooping(true);
			this.backgroundMusic.setVolume(.7f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		//physicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
		
		
		scene = new Scene();
		scene.setOnSceneTouchListener(this);
		
//		Rectangle ground = new Rectangle(0, 0, CAMERA_WIDTH, 2, this.getVertexBufferObjectManager());
//		ground.setColor(Color.RED);
//		scene.attachChild(ground);
//		PhysicsFactory.createBoxBody(physicsWorld, ground, BodyType.StaticBody, WALL_FIX);
		
		scene.registerUpdateHandler(physicsWorld);
		
		AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-20.0f, new Sprite(.5f*CAMERA_WIDTH , .5f*CAMERA_HEIGHT, this.backGroundRegion, this.getVertexBufferObjectManager())));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-20.0f, new Sprite(.5f*CAMERA_WIDTH, .5f*CAMERA_HEIGHT+80, this.midGroundRegion, this.getVertexBufferObjectManager())));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-100.0f, new Sprite(.5f*CAMERA_WIDTH, .5f*this.frontGroundRegion.getHeight(), this.frontGroundRegion, this.getVertexBufferObjectManager())));
		scene.setBackground(autoParallaxBackground);
//		scene.setBackground(new Background(Color.BLUE));
		
		final float playerX = 100;
		final float playerY = 85;

        ResourceManager.prepareManager(this.getEngine(), this, camera, getVertexBufferObjectManager());
        ResourceManager.getInstance().loadGameResources();
		
		this.player = new Player(playerX, playerY, this.getVertexBufferObjectManager(), camera, physicsWorld, 3);
		this.player.animate(100);
		this.player.setRunning();
		camera.setChaseEntity(player);
//		player.body = PhysicsFactory.createBoxBody(physicsWorld, player, BodyType.DynamicBody, PLAYER_FIX);
//		player.body.setLinearVelocity(new Vector2(5, player.body.getLinearVelocity().y));
		scene.attachChild(player);
		
		
		
		loadLevel("one");
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(player, this.player.body, true, false));
		this.backgroundMusic.play();
		return scene;
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
                            performAction(difX, difY, moveDistance);
                            actionPerformed = true;
//                            lastX = pSceneTouchEvent.getX();
//                            lastY = pSceneTouchEvent.getY();
                        }
                        break;
                    case TouchEvent.ACTION_UP:
                        if (!actionPerformed) {
                            performAction(difX, difY, moveDistance);
                            actionPerformed = true;
                        }
                        break;
                }
            }
            return true;


//			if (pSceneTouchEvent.isActionDown() && Math.abs(playerBody.getLinearVelocity().y) == 0)
//		    {
//                    this.playerBody.getLocalPoint().x;
//		        	this.playerBody.setLinearVelocity(playerBody.getLinearVelocity().x, 10.0f);
//		        	System.out.println();
//		    }

        }
        return false;
	}

    private void performAction(float difX, float difY, double moveDistance) {
        if (difX > 0 && difX > Math.abs(difY)) {
            player.dash();
        }
        if (difY < 0 && Math.abs(difY) > Math.abs(difX)) {
            player.duck();
        }
        if (difY > 0 && Math.abs(difY) > Math.abs(difX)|| moveDistance <= TAP_THRESHOLD) {
            this.jumpSound.play();
            player.jump();
        }
    }

	private void loadLevel(String levelID) {
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(this.getVertexBufferObjectManager());
		
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {

			@Override
			public IEntity onLoadEntity(String pEntityName, IEntity pParent, Attributes pAttributes, SimpleLevelEntityLoaderData pEntityLoaderData)	throws IOException {
				final int level_width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int level_height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				
				camera.setBounds(0, 0, level_width, level_height);
				camera.setBoundsEnabled(true);
				
				return scene;
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
				
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_OBSTACLE1)) {
					levelObject = new Rectangle(x, y, width, height, mEngine.getVertexBufferObjectManager());
					levelObject.setColor(Color.GREEN);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GROUND1)) {
					levelObject = new Rectangle(x, y, width, height, mEngine.getVertexBufferObjectManager());
					levelObject.setColor(Color.RED);
				} else {
					throw new IllegalArgumentException();
				}
				
				physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, WALL_FIX), true, false));
				
				return levelObject;
			}
			
		});
		
		levelLoader.loadLevelFromAsset(this.getAssets(), "level/" + levelID + ".xml");
	}
}
