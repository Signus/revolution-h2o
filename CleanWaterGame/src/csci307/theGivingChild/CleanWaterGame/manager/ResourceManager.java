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

import csci307.theGivingChild.CleanWaterGame.CleanWaterGame;
import csci307.theGivingChild.CleanWaterGame.GameLauncher;

import android.app.Activity;
import android.graphics.Color;

public class ResourceManager {
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	public Engine engine;
	public BaseGameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Font font, game_font;
    private BitmapTextureAtlas spriteAtlas;
	
	//------------------------------------------------
	//TEXTURE & TEXTURE REGIONS
	//------------------------------------------------
	
	//splash textures
	public ITextureRegion splash_icon_TR;
	
	//menu textures
	public ITextureRegion act_menu_background_TR;
	public ITextureRegion menu_background_TR;
	public ITextureRegion scene_one_TR, scene_two_TR, scene_three_TR, scene_four_TR, scene_five_TR;
	public ITextureRegion act_one_TR;
	
	//game textures
	public ITiledTextureRegion player_TR, player_jump_TR, player_slide_TR;
	public ITextureRegion collectable_TR,twine_TR,wood_TR,stone_TR;
	public ITextureRegion scene_background_TR;
	public ITextureRegion scene_foreground_TR;
	public ITextureRegion pause_TR, hitpoints_TR;
	public ITextureRegion ground_TR, floating_platform_ground_TR, hill_TR, falling_platform_2_TR, falling_platform_TR;
	
	//texture atlas
	private BitmapTextureAtlas splashTA;
	private BuildableBitmapTextureAtlas menuTA;
	private BuildableBitmapTextureAtlas gameTA;
	private BuildableBitmapTextureAtlas groundTA;
	
	//sounds
	public Sound jumpSound;
	public Sound dashSound;
	public Sound duckSound;
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
		loadGameFonts();
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
		scene_three_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_three_button.png");
		scene_four_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_four_button.png");
		scene_five_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "scene_five_button.png");
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
	
	/**
	 * This toggles the mute value in the shared preferences object should allow for global mute setting
	 */
	public void toggleMute()
	{
		boolean efx = CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, Activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_MUTE, false);
		CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, Activity.MODE_MULTI_PROCESS).edit().putBoolean(GameLauncher.PREFERENCE_KEY_MUTE, (efx ? false : true)).commit();
	}
	
	/**
	 * This returns the value currently saved in the shared preferences listener regarding mute
	 * @return true if it is muted, false if unmuted
	 */
	public boolean isMuted()
	{
		return CleanWaterGame.getInstance().getSharedPreferences(GameLauncher.PREFERENCE_KEY, Activity.MODE_MULTI_PROCESS).getBoolean(GameLauncher.PREFERENCE_KEY_MUTE, false);
	}
	
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");

        gameTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.BILINEAR);

        player_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTA, activity, "player_run_sprite.png", 6, 1);
        player_jump_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTA, activity, "roll_jump.png", 1, 1);
        player_slide_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTA, activity, "roll_slide.png", 2, 1);
        hitpoints_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "heart.png");
        collectable_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "water.png");
        twine_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "twine.png");
        wood_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "wood.png");
        stone_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "stone.png");

        pause_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "pause_button.png");
        scene_background_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "gradient_background.png");
        scene_foreground_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTA, activity, "clouds.png");

        groundTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT);
        ground_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "ground.png");
        floating_platform_ground_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "floating_platform_ground.png");
        hill_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "hill.png");
        falling_platform_2_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "falling_platform_small.png");
        falling_platform_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(groundTA, activity, "falling_platform_large.png");
        try {
        	this.gameTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
        	this.groundTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
        } catch (final TextureAtlasBuilderException e) {
        	Debug.e(e);
        }
        this.gameTA.load();
        this.groundTA.load();
    }
	
	private void loadGameFonts() {
		FontFactory.setAssetBasePath("fonts/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 40, true, Color.WHITE, 2, Color.BLACK);
		game_font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "NeverwinterNights.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
		game_font.load();
	}
	
	private void loadGameAudio() {
		//SOUND
		SoundFactory.setAssetBasePath("sfx/");
		try {
			this.jumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity, "jump.mp3");
			this.jumpSound.setVolume(.5f);
			this.dashSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity, "dash.mp3");
			this.dashSound.setVolume(.7f);
			this.duckSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(),activity, "duck.mp3");
			this.duckSound.setVolume(.3f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//MUSIC
		MusicFactory.setAssetBasePath("sfx/");
		try {
			this.backgroundMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(),activity, "gameMusic.mp3");
			this.backgroundMusic.setLooping(true);
			this.backgroundMusic.setVolume(.9f);
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
