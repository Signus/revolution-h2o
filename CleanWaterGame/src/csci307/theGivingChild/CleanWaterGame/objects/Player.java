package csci307.theGivingChild.CleanWaterGame.objects;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;

public class Player extends AnimatedSprite {
	// VARIABLES
	public Body body;
	private boolean canRun = false;
    private float runSpeed = 5;

    private static final int MAX_SPRINT = 100;
    private static final float SPRINT_AUGMENT = 4;
    private int sprintTime = MAX_SPRINT;
    private boolean isSprinting = false;

    public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourceManager.getInstance().player_TR, vbom);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, 36, 50, 65, 100, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		//body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setUserData("player");
		body.setFixedRotation(true);
		
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
		{
			@Override
			public void onUpdate(float pSecondsElapsed)
			{
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);

				if (getY() <= 0)
				{
					onDie();
				}

				if (canRun)
				{
					body.setLinearVelocity(new Vector2(runSpeed, body.getLinearVelocity().y));
                    if (isSprinting) {
                        sprintTime--;
                        if (sprintTime <= 0) {
                            sprintTime = MAX_SPRINT;
                            isSprinting = false;
                            runSpeed -= SPRINT_AUGMENT;
                        }
                    }
				}
			}

		});
	}
	
	public void setRunning() {
		canRun = true;
		
//		final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };
//		
//		animate(PLAYER_ANIMATE, 0, 2, true);
		animate(100);
	}
	
	public void jump() {
        body.setLinearVelocity(body.getLinearVelocity().x, 10.0f);
	}

	public void dash() {
        if (isSprinting) return;
        isSprinting = true;
        runSpeed += SPRINT_AUGMENT;
	}

	public void duck() {

	}

	public void onDie() {

    }
	
	public void setAnimation(AnimatedSprite selectedAnimatedSprite) {
		AnimatedSprite sprite = new AnimatedSprite(selectedAnimatedSprite.getX(), selectedAnimatedSprite.getY(),
				selectedAnimatedSprite.getTiledTextureRegion().deepCopy(),
				selectedAnimatedSprite.getVertexBufferObjectManager());
		
	}

    // Not fully correct
    public boolean isNotPerformingAction() {
        // If jumping...
        if (Math.abs(body.getLinearVelocity().y) != 0) return false;

        return true;
    }

}
