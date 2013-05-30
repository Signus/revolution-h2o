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
    private static final float JUMP_VELOCITY = 10.0f;
    private static final float JUMP_TOLERANCE = 1.0f;
    // VARIABLES
	public Body body;
	private boolean canRun = false;
    private float runSpeed = 5;
    private static final int MAX_SPRINT = 100;
    private static final float SPRINT_AUGMENT = 4;
    private int sprintTime = MAX_SPRINT;
    private static final int MAX_DUCK = 100;
    private int duckTime = MAX_DUCK;
    private boolean isSprinting = false;
    private boolean isJumping = false;
    private boolean isDucking = false;
    private static int TIME = 100;

    final long[] PLAYER_ANIMATE = new long[] {TIME, TIME, TIME, TIME, TIME, TIME};
    private static PhysicsWorld physicsWorld;

    public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourceManager.getInstance().player_TR, vbom);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}
    
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
        Player.physicsWorld = physicsWorld;
		body = PhysicsFactory.createBoxBody(physicsWorld, 36, 50, 65, 100, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        //body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setUserData("player");
		body.setFixedRotation(true);
        //newBody(50);

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
                            setToInitialSprite();
                        }
                    }
                    if (isJumping) {
                        if (!verticalMotion()) {
                            isJumping = false;
                            setToInitialSprite();
                        }
                    }
                    if (isDucking) {
                        duckTime--;
                        if (duckTime <= 0) {
                            duckTime = MAX_DUCK;
                            isDucking = false;
                            newBody(5);
                            setToInitialSprite();
                        }

                    }
                }
			}

		});
	}

    private void setToInitialSprite() {
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100 };
        animate(PLAYER_ANIMATE, 0, 5, true);
    }

    // Need to change for new sprites
    private void setToJumpSprite() {
        final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
        animate(PLAYER_ANIMATE, 0, 1, true);
    }

    private void setToDashSprite() {
    }

    private void setToDuckSprite() {
        final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
        animate(PLAYER_ANIMATE, 2, 3, true);
    }

    public void setRunning() {
		canRun = true;
        setToInitialSprite();

//		animate(100);
	}
	
	public void jump() {
        //if (isJumping) return;
        isJumping = true;
        setToJumpSprite();

        body.setLinearVelocity(body.getLinearVelocity().x, JUMP_VELOCITY);
	}

	public void dash() {
        if (isSprinting) return;
        isSprinting = true;
        setToDashSprite();

        //dash sprite

        runSpeed += SPRINT_AUGMENT;
	}

    public void duck() {
        if (isDucking) return;
        isDucking = true;
        setToDuckSprite();
        newBody(-5);
    }

	public void onDie() {
		
    }

    private boolean verticalMotion() {
        return Math.abs(body.getLinearVelocity().y) != 0 && body.getLinearVelocity().y > -JUMP_VELOCITY + JUMP_TOLERANCE;
    }

    // Not fully correct
    public boolean isNotPerformingAction() {
        // If jumping...
        if (verticalMotion()) return false;
        if (isDucking) return false;

        return true;
    }

    private void newBody(float height) {
        //body.setTransform(body.getPosition().x, body.getPosition().y + height, 0);
//        body = PhysicsFactory.createBoxBody(physicsWorld, 36, 50, 65, height, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
//        body.setUserData("player");
//        body.setFixedRotation(true);
    }
}
