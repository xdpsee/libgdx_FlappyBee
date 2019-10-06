package com.zhenhui.libgdx.flappybee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class FlappyBee implements Disposable {

    private static final int COLLISION_RADIUS = 24;

    private static final float DIVE_ACCEL = 0.25f;

    private static final float FLAY_ACCEL = 3f;

    private float x;

    private float y;

    private float ySpeed = 0;

    private Circle collisionCircle;

    private Texture[] frames = new Texture[6];

    private Animation<Texture> animation;

    private float stateTime;

    public FlappyBee(float x, float y) {
        this.x = x;
        this.y = y;
        this.collisionCircle = new Circle(x, y, COLLISION_RADIUS);

        for (int i = 1; i <= 6; ++i) {
            frames[i - 1] = new Texture(Gdx.files.internal(String.format("%d.png", i)));
        }

        animation = new Animation<>(0.05f, new Array<>(frames), Animation.PlayMode.LOOP);
    }

    @Override
    public void dispose() {
        for (Texture frame : frames) {
            frame.dispose();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.collisionCircle.setPosition(x, y);
    }

    public void flayUp() {
        ySpeed = FLAY_ACCEL;

        setPosition(x, y + ySpeed);
    }

    public void update(float delta) {

        stateTime += delta;

        ySpeed -= DIVE_ACCEL;

        setPosition(x, MathUtils.clamp(y + ySpeed, 24, Constants.WORLD_HEIGHT));
    }

    public void drawDebug(ShapeRenderer renderer) {
        if (renderer != null) {
            renderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
        }
    }

    public void draw(SpriteBatch batch) {

        Texture keyFrame = animation.getKeyFrame(stateTime);
        batch.draw(keyFrame
                , x - collisionCircle.radius
                , y - collisionCircle.radius
                , collisionCircle.radius * 2
                , collisionCircle.radius * 2);

    }

    public float getX() {
        return x;
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }
}

