/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This is the base for all scenes menus level etc..
*
* History:
*    5/31/13 original
*/
package csci307.theGivingChild.CleanWaterGame.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.manager.SceneManager.SceneType;

import android.app.Activity;



public abstract class BaseScene extends Scene
{
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
    
    public BaseScene()
    {
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
    
    public abstract void createScene();
    
    public abstract void onBackKeyPressed();
    
    public abstract SceneType getSceneType();
    
    public abstract void disposeScene();

}