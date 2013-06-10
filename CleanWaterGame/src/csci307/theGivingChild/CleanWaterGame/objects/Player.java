package csci307.theGivingChild.CleanWaterGame.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import csci307.theGivingChild.CleanWaterGame.manager.ResourceManager;
import csci307.theGivingChild.CleanWaterGame.scene.GameScene;
import org.andengine.engine.camera.Camera;
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
    private float runSpeed = 5;
    private static final int MAX_SPRINT = 100;
    private static final float SPRINT_AUGMENT = 4;
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
					onDie();
				}

				if (canRun)
				{
					body.setLinearVelocity(new Vector2(runSpeed, body.getLinearVelocity().y));
                    if (isSprinting) {
                        sprintTime--;
                        if (sprintTime <= 0 && !verticalMotion()) {
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
                        System.out.println(duckTime);
                        if (duckTime <= 0) {
                            duckTime = MAX_DUCK;
                            isDucking = false;
                            newBody(5);
                            setToInitialSprite();

                        }

                    }

                }
				if (isBouncing) {
                	if (!verticalMotion()) {
                		isBouncing = false;
                		setToInitialSprite();
                		canRun = true;
                		body.setLinearVelocity(runSpeed, body.getLinearVelocity().y);
                	}
                }
			}

		});
	}

	public boolean isDucking() {
		return isDucking;
	}

	public void setIsDucking(boolean ducking) {
		isDucking = ducking;
	}

    private void setToInitialSprite() {
//        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100, 100, 100, 100 };
//        animate(PLAYER_ANIMATE, 0, 5, true);
    	animate(100);
    }

    // Need to change for new sprites
    private void setToJumpSprite() {
//        final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
//        animate(PLAYER_ANIMATE, 0, 1, true);
        animate(100);
    }

    private void setToDashSprite() {
//        final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
//        animate(PLAYER_ANIMATE, 4, 5, true);
    	animate(100);
    }

    private void setToDuckSprite() {
//        final long[] PLAYER_ANIMATE = new long[] { 100, 100 };
//        animate(PLAYER_ANIMATE, 2, 3, true);
    	animate(100);
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
       if(!ResourceManager.getInstance().isMuted()) ResourceManager.getInstance().jumpSound.play();

        body.setLinearVelocity(body.getLinearVelocity().x, JUMP_VELOCITY);
	}

	public void dash() {
        if (isSprinting) return;
        isSprinting = true;
        setToDashSprite();
        if(!ResourceManager.getInstance().isMuted())ResourceManager.getInstance().dashSound.play();

        //dash sprite

        runSpeed += SPRINT_AUGMENT;
	}

    public void duck() {
        if (isDucking) return;
        isDucking = true;
        //enable ducking sound
        //if(!ResourceManager.getInstance().isMuted()) ResourceManager.getInstance().duckSound.play();
        setToDuckSprite();
        body.setLinearVelocity(runSpeed, body.getLinearVelocity().y);
        newBody(-5);
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

    private void newBody(float height) {
        //body.setTransform(body.getPosition().x, body.getPosition().y + height, 0);
//        body = PhysicsFactory.createBoxBody(physicsWorld, 36, 50, 65, height, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
//        body.setUserData("player");
//        body.setFixedRotation(true);
	}

    public void decrementHP() {
    	hitpoints--;
    }

    public int getHP() {
    	return hitpoints;
    }
//    private void switchBody() {
//        if (position == BodyPosition.NORMAL) {
////            duckingBody = PhysicsFactory.createBoxBody(physicsWorld, this.getX(), this.getY(), this.getWidth(), this.getHeight() / 2, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
//            position = BodyPosition.DUCKING;
//        } else {
//            position = BodyPosition.NORMAL;
//        }
//            //body.setTransform(body.getPosition().x, body.getPosition().y + height, 0);
////        body = PhysicsFactory.createBoxBody(physicsWorld, 36, 50, 65, height, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
////        body.setUserData("player");
////        body.setFixedRotation(true);
//    }
//
//    private Body getBody() {
//        if (position == BodyPosition.NORMAL) {
//            return;
//        }
//    }
}
