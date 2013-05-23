package csci307.theGivingChild.CleanWaterGame.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;

public abstract class Player extends AnimatedSprite {
	
	private Body body;
	
	public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourceManager.getInstance().player_TR, vbom);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		
	}
	
	public void setRunning() {
		
	}
	
	public void jump() {
		
	}
	
	public void dash() {
		
	}
	
	public void duck() {
		
	}
	
	public abstract void onDie();

}
