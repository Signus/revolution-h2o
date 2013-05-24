/*
 * Authors: Chris Card, Tony Nguyen, Gurpreet Nanda, Dylan Chau, Dustin Liang, Maria Deslis
 * Date: 05/22/13
 * Description: Resource Manager Class
 * 				responsible for loading and unloading resources a current scene needs. 
 */

package csci307.theGivingChild.CleanWaterGame.manager;

import android.app.Activity;
import android.graphics.Color;
import csci307.theGivingChild.CleanWaterGame.ActOneActivity;
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
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

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
	public ITextureRegion menu_background_TR;
	public ITextureRegion scene_one_TR;
	public ITextureRegion act_one_TR;
	
	//game textures
	public ITiledTextureRegion player_TR;
	
	//texture atlas
	private BitmapTextureAtlas splashTA;
	private BuildableBitmapTextureAtlas menuTA;
	private BuildableBitmapTextureAtlas gameTA;
	
	
	
	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuFonts();
	}
	
	

	public void loadGameResources() {
		loadGameGraphics();
		//loadGameFonts();
		//loadGameAudio();
	}
	
	public void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		menuTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
//		play_button_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "play.png");
//		options_button_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "options.png");
		scene_one_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "button.png");
		menu_background_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "parallax_background_layer_back.png");
		act_one_TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, activity, "temp_button.png");
		
		try {
			this.menuTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTA.load();
		}
		catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}
	
	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("fonts/");		
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 50, true, Color.WHITE, 2, Color.BLACK);
		font.load();
	}
	
	public void unloadMenuTextures() {
		menuTA.unload();
	}
	
	public void loadMenuTextures() {
		menuTA.load();
	}
	
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//        this.spriteAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1024, 128, TextureOptions.BILINEAR);
//        this.player_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(spriteAtlas, activity, "nyan_cat_sprite.png", 0, 0, 6, 1);

        this.gameTA = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 128, TextureOptions.BILINEAR);
        this.player_TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTA, activity, "player_run_sprite.png", 6, 1);
        
        try {
        	this.gameTA.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
        } catch (final TextureAtlasBuilderException e) {
        	Debug.e(e);
        }
        this.gameTA.load();
    }
	
	public void unloadGameTextures() {
		
	}
	
	public void loadSplashScreen() {
		
	}
	
	public void unloadSplashScreen() {
	//	splashTA.unload();
	//	splash_icon_TR = null;
	}
	
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
