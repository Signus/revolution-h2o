/**
* Authors: Chris Card, Dylan Chau, Maria Deslis, Dustin Liang, Gurpreet Nanda, Tony Nguyen
* Date: 5/31/13
* Version: 1.0
* Description: This contains the character actions sounds and images
*
* History:
*    5/31/13 original
*/
package csci307.theGivingChild.CleanWaterGame.objects;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.scene.GameScene;

public class Player extends AnimatedSprite {
    private enum BodyPosition { NORMAL, DUCKING }
    private BodyPosition position;
    private static final float JUMP_VELOCITY = 10.0f;
    private static final float JUMP_TOLERANCE = 0.25f;
    private static int TIME = 100;
    private static PhysicsWorld physicsWorld;
    final long[] PLAYER_ANIMATE = new long[]{TIME, TIME, TIME, TIME, TIME, TIME};
    // VARIABLES
	public Body body;
	private boolean canRun = false;
    private static final float SPRINT_SPEED = 9;
    private static final float NORMAL_SPEED = 5;
    private float runSpeed = NORMAL_SPEED;
    private static final int MAX_SPRINT = 100;
    private int sprintTime = MAX_SPRINT;
    private static final int MAX_DUCK = 10;
    private int duckTime = MAX_DUCK;
    private boolean isSprinting = false;
    private boolean isJumping = false;
    private boolean isDucking = false;
    private boolean isBouncing = false;
    private int hitpoints = 0;

    public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld, int hp, ITiledTextureRegion region) {
		super(pX, pY, region, vbom);
		createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
        initializeBooleanConditions();
        hitpoints = hp;
        position = BodyPosition.NORMAL;
    }

    private void initializeBooleanConditions() {
        canRun = false;
        isSprinting = false;
        isJumping = false;
        isDucking = false;
        isBouncing = false;
    }


	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
        Player.physicsWorld = physicsWorld;
		//body = PhysicsFactory.createBoxBody(physicsWorld, 40.25f, 50, 80.5f, 100, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, GameScene.PLAYER_FIX);
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
					hitpoints = 0;
					onDie();
				}

				if (hitpoints <= 0){
					onDie();
				}

				if (canRun)
				{
					body.setLinearVelocity(new Vector2(runSpeed, body.getLinearVelocity().y));
                    if (isSprinting) {
                        sprintTime--;
                        if (sprintTime <= 0 && !verticalMotion()) {
                            resetSprint();
                        }
                    }
                    if (isJumping) {
                        if (!verticalMotion()) {
                            isJumping = false;
                            setToInitialSprite();
                        }
                    }
                }
				if (isBouncing) {
                	if (!verticalMotion()) {
                		isBouncing = false;
                		setToInitialSprite();
                		canRun = true;
                        resetSprint();
                		body.setLinearVelocity(runSpeed, body.getLinearVelocity().y);
                	}
                }
			}

		});
	}

    private void resetSprint() {
        sprintTime = MAX_SPRINT;
        isSprinting = false;
        runSpeed = NORMAL_SPEED;
        setToInitialSprite();
    }

	public boolean isDucking() {
		return isDucking;
	}

	public void setIsDucking(boolean ducking) {
		isDucking = ducking;
	}

    private void setToInitialSprite() {
        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100 };
        animate(PLAYER_ANIMATE, 0, 5, true);
    }

    // Need to change for new sprites
    private void setToJumpSprite() {
        final long[] PLAYER_ANIMATE = new long[] { 100, 10000 };
        animate(PLAYER_ANIMATE, 5, 6, true);
    }

    private void setToDashSprite() {
        final long[] PLAYER_ANIMATE = new long[] { 50, 50, 50, 50, 50, 50 };
        animate(PLAYER_ANIMATE, 0, 5, true);
    }

    public void setRunning() {
		canRun = true;
        setToInitialSprite();
	}

	public void jump() {
        //if (isJumping) return;
        isJumping = true;
        setToJumpSprite();
       if(!ResourceManager.getInstance().isMuted()) ResourceManager.getInstance().jumpSound.play();

        body.setLinearVelocity(body.getLinearVelocity().x, JUMP_VELOCITY);
	}

	public void dash() {
        if (isSprinting) return;
        isSprinting = true;
        setToDashSprite();
        if(!ResourceManager.getInstance().isMuted())ResourceManager.getInstance().dashSound.play();

        //dash sprite

        runSpeed = SPRINT_SPEED;
	}

    public void bounceBack() {
    	if (isBouncing) return;
    	body.setLinearVelocity(-runSpeed, JUMP_VELOCITY / 2.0f);
    	isBouncing = true;
    	canRun = false;

    }

	public void onDie() {

    }

    // TODO: Refactor for clarity
    private boolean verticalMotion() {
        return Math.abs(body.getLinearVelocity().y) != 0 && (body.getLinearVelocity().y > -JUMP_VELOCITY + JUMP_TOLERANCE || body.getLinearVelocity().y < -JUMP_VELOCITY );
    }

    // Not fully correct
    public boolean isNotPerformingAction() {
        // If jumping...
        if (verticalMotion() || isJumping) return false;
        if (isDucking) return false;

        return true;
    }

    public void decrementHP() {
    	hitpoints--;
    }

    public void setHP(int hp) {
    	hitpoints = hp;
    }

    public int getHP() {
    	return hitpoints;
    }
}
