package com.zhenhui.libgdx.flappybee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class Flower implements Disposable {

    private static final float MAX_SPEED_PER_SECOND = 100F;

    private static final float COLLISION_CIRCLE_RADIUS = 33f;

    private static final float COLLISION_RECTANGLE_WIDTH = 12f;

    private static final float COLLISION_RECTANGLE_HEIGHT = 447f;

    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;

    private static final float HEIGHT_OFFSET = -400f;

    private static final float DISTANCE_BETWEEN_FLOOR_AND_CEILING = 225f;

    private float x;

    private float y;

    private Circle collisionCircle;

    private Rectangle collisionRectangle;

    private Circle ceilingCollisionCircle;

    private Rectangle ceilingCollisionRectangle;

    private Texture texture;

    private Texture stem;

    public Flower() {
        this.y = MathUtils.random(HEIGHT_OFFSET);
        collisionRectangle = new Rectangle(x
                , y
                , COLLISION_RECTANGLE_WIDTH
                , COLLISION_RECTANGLE_HEIGHT);
        collisionCircle = new Circle(x + COLLISION_RECTANGLE_WIDTH / 2
                , y + COLLISION_RECTANGLE_HEIGHT
                , COLLISION_CIRCLE_RADIUS);
        ceilingCollisionRectangle = new Rectangle(x
                , collisionRectangle.y + COLLISION_RECTANGLE_HEIGHT + DISTANCE_BETWEEN_FLOOR_AND_CEILING
                , COLLISION_RECTANGLE_WIDTH
                , COLLISION_RECTANGLE_HEIGHT);
        ceilingCollisionCircle = new Circle(collisionCircle.x
                , ceilingCollisionRectangle.y
                , COLLISION_CIRCLE_RADIUS);
        texture = new Texture(Gdx.files.internal("flower.png"));
        stem = new Texture(Gdx.files.internal("stem.png"));
    }

    @Override
    public void dispose() {
        texture.dispose();
        stem.dispose();
    }

    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    public float getX() {
        return x;
    }

    public void setPosition(float x) {
        this.x = x;

        collisionRectangle.setX(x);
        ceilingCollisionRectangle.setX(x);
        collisionCircle.setX(x + COLLISION_RECTANGLE_WIDTH / 2);
        ceilingCollisionCircle.setX(x + COLLISION_RECTANGLE_WIDTH / 2);
    }

    public boolean isFlappyBeeColliding(FlappyBee flappyBee) {

        Circle flappyBeeCircle = flappyBee.getCollisionCircle();
        return Intersector.overlaps(flappyBeeCircle, collisionCircle)
                || Intersector.overlaps(flappyBeeCircle, collisionRectangle)
                || Intersector.overlaps(flappyBeeCircle, ceilingCollisionCircle)
                || Intersector.overlaps(flappyBeeCircle, ceilingCollisionRectangle);

    }

    public void drawDebug(ShapeRenderer renderer) {

        if (renderer != null) {
            renderer.rect(collisionRectangle.x
                    , collisionRectangle.y
                    , collisionRectangle.width
                    , collisionRectangle.height);

            renderer.circle(collisionCircle.x
                    , collisionCircle.y
                    , collisionCircle.radius);

            renderer.rect(ceilingCollisionRectangle.x
                    , ceilingCollisionRectangle.y
                    , ceilingCollisionRectangle.width
                    , ceilingCollisionRectangle.height);

            renderer.circle(ceilingCollisionCircle.x
                    , ceilingCollisionCircle.y
                    , ceilingCollisionCircle.radius);
        }
    }

    public void draw(SpriteBatch batch) {

        batch.draw(stem
                , collisionRectangle.x
                , collisionRectangle.y
                , COLLISION_RECTANGLE_WIDTH
                , COLLISION_RECTANGLE_HEIGHT);
        batch.draw(stem
                , ceilingCollisionRectangle.x
                , ceilingCollisionRectangle.y
                , COLLISION_RECTANGLE_WIDTH
                , COLLISION_RECTANGLE_HEIGHT);
        batch.draw(texture
                , collisionCircle.x - COLLISION_CIRCLE_RADIUS
                , collisionCircle.y - COLLISION_CIRCLE_RADIUS
                , COLLISION_CIRCLE_RADIUS * 2
                , COLLISION_CIRCLE_RADIUS * 2);

        batch.draw(texture
                , ceilingCollisionCircle.x - COLLISION_CIRCLE_RADIUS
                , ceilingCollisionCircle.y - COLLISION_CIRCLE_RADIUS
                , COLLISION_CIRCLE_RADIUS * 2
                , COLLISION_CIRCLE_RADIUS * 2);

    }
}
