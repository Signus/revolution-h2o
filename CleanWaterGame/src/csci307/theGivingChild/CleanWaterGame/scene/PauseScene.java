package csci307.theGivingChild.CleanWaterGame.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.app.Activity;
import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;

public class PauseScene extends CameraScene {
	

	//---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    protected Engine engine;
    protected Activity activity;
    protected ResourceManager resourcesManager;
    protected VertexBufferObjectManager vbom;
    protected BoundCamera camera;
    
    //---------------------------------------------
    // CONSTRUCTOR
    //---------------------------------------------
    
    public PauseScene() { 
        this.resourcesManager = ResourceManager.getInstance();
        this.engine = resourcesManager.engine;
        this.activity = resourcesManager.activity;
        this.vbom = resourcesManager.vbom;
        this.camera = resourcesManager.camera;
        createScene();
    }
    
    
    //---------------------------------------------
    // ABSTRACTION
    //---------------------------------------------
    
    public void createScene() {
    	setBackground(new Background(Color.BLACK));
	}
    
    public void onBackKeyPressed() {
	}
    
    public SceneType getSceneType() {
		return null;
	}
    
    public void disposeScene() {
	}


}
