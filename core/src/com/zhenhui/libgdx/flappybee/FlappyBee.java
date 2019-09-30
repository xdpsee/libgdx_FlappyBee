package com.zhenhui.libgdx.flappybee;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class FlappyBee {

    private static final int COLLISION_RADIUS = 24;

    private static final float DIVE_ACCEL = 0.25f;

    private static final float FLAY_ACCEL = 3f;

    private float x;

    private float y;

    private float ySpeed = 0;

    private Circle collisionCircle;

    public FlappyBee(float x, float y) {
        this.x = x;
        this.y = y;
        this.collisionCircle = new Circle(x, y, COLLISION_RADIUS);
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

    public void update() {

        ySpeed -= DIVE_ACCEL;

        setPosition(x, y + ySpeed);

    }

    public void drawDebug(ShapeRenderer renderer) {
        if (renderer != null) {
            renderer.circle(collisionCircle.x, collisionCircle.y, collisionCircle.radius);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

