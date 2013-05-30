/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/22/13
 * Description: Resource Manager Class
 * 				responsible for loading and unloading resources a current scene needs. 
 */

package csci307.theGivingChild.CleanWaterGame.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

public class ResourceManager {
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	public Engine engine;
	public BaseGameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Font font;
    private BitmapTextureAtlas spriteAtlas;
	
	//------------------------------------------------
	//TEXTURE & TEXTURE REGIONS
	//------------------------------------------------
	
	//splash textures
	public ITextureRegion splash_icon_TR;
	
	//menu textures
	public ITextureRegion act_menu_background_TR;
	public ITextureRegion menu_background_TR;
	public ITextureRegion scene_one_TR;
	public ITextureRegion scene_two_TR;
	public ITextureRegion act_one_TR;
	
	//game textures
	public ITiledTextureRegion player_TR;
	public ITextureRegion collectable_TR;
	public ITextureRegion pause_TR;
	
	//texture atlas
	private BitmapTextureAtlas splashTA;
	private BuildableBitmapTextureAtlas menuTA;
	private BuildableBitmapTextureAtlas gameTA;
	
	//sounds
	public Sound jumpSound;
	
	//music
	public Music backgroundMusic;
	
	//------------------------------------------------
	//CALLS TO LOAD ALL RESOURCES
	//------------------------------------------------
	
	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuFonts();
	}
	
	public void loadGameResources() {
		loadGameGraphics();
		//loadGameFonts();
		loadGameAudio();
	}
	
	//------------------------------------------------
	//SPLASH RESOURCES
	//------------------------------------------------
	
	public void loadSplashScreen() {
		
	}
	
	//------------------------------------------------
	//MENU RESOURCES
	//------------------------------------------------
	
	public void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);
		scene_one_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_one_button.png");
		scene_two_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_two_button.png");
		menu_background_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "act_one_background.png");
		act_menu_background_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "act_menu_background.png");
		act_one_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "act_one_button.png");
		
		try {
			this.menuTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			this.menuTA.load();
		}
		catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}
	
	public void loadMenuFonts() {
		FontFactory.setAssetBasePath("fonts/");		
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}
	
	//------------------------------------------------
	//GAME RESOURCES
	//------------------------------------------------
	
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

        gameTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 128, TextureOptions.BILINEAR);
        player_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTA, activity, "player_run_sprite.png", 6, 1);
        collectable_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "coin.png");
        pause_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "button.png");
        
        
        try {
        	this.gameTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
        } catch (final TextureAtlasBuilderException e) {
        	Debug.e(e);
        }
        this.gameTA.load();
    }
	
	private void loadGameAudio() {
		//SOUND
		SoundFactory.setAssetBasePath("sfx/");
		try {
			this.jumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity, "jump.mp3");
			this.jumpSound.setVolume(.5f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//MUSIC
		MusicFactory.setAssetBasePath("sfx/");
		try {
			this.backgroundMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(),activity, "jungle.mp3");
			this.backgroundMusic.setLooping(true);
			this.backgroundMusic.setVolume(.7f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//------------------------------------------------
	//UNLOADING RESOURCES
	//------------------------------------------------
	
	public void unloadSplashScreen() {
	//	splashTA.unload();
	//	splash_icon_TR = null;
	}
	
	public void unloadMenuGraphics() {
		menuTA.unload();
	}
	
	private void unloadMenuFonts() {
		font.unload();
	}
	
	public void unloadGameGraphics() {
		gameTA.unload();
	}
	
	//------------------------------------------------
	//MANAGER FUNCTIONS
	//------------------------------------------------

	public static void prepareManager(Engine engine, BaseGameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
	}
	
	public static ResourceManager getInstance() {
		return INSTANCE;
	}

}
