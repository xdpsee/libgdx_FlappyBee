package com.zhenhui.libgdx.flappybee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {

    private static final float GAP_BETWEEN_FLOWERS = 200F;

    private Viewport viewport;

    private Camera camera;

    private ShapeRenderer shapeRenderer;

    private SpriteBatch batch;

    //
    private FlappyBee flappyBee;

    private Array<Flower> flowers = new Array<>();

    public GameScreen() {
        camera = new OrthographicCamera();
        camera.position.set(new Vector2(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2), 0);
        camera.update();

        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        flappyBee = new FlappyBee(0, 0);
        flappyBee.setPosition(Constants.WORLD_WIDTH / 4, Constants.WORLD_HEIGHT / 2);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
    }

    @Override
    public void render(float delta) {

        update(delta);

        clearScreen();

        drawDebug();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();

        batch.end();
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        flappyBee.drawDebug(shapeRenderer);

        for (Flower flower : flowers) {
            flower.drawDebug(shapeRenderer);
        }

        shapeRenderer.end();
    }

    private void update(float delta) {

        flappyBee.update();

        updateFlowers(delta);

        checkForCollision();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            flappyBee.flayUp();
        }

        blackFlappyBeeLeaveTheWorld();
    }

    private void updateFlowers(float delta) {

        for (Flower flower : flowers) {
            flower.update(delta);
        }

        checkIfNewFlowerIsNeeded();
        removeFlowersIfPassed();
    }

    private void clearScreen() {
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void checkIfNewFlowerIsNeeded() {
        if (flowers.isEmpty()) {
            createNewFlower();
        } else {
            Flower flower = flowers.peek();
            if (flower.getX() < Constants.WORLD_WIDTH - GAP_BETWEEN_FLOWERS) {
                createNewFlower();
            }
        }
    }

    private void checkForCollision() {

        for (Flower flower : flowers) {
            if (flower.isFlappyBeeColliding(flappyBee)) {
                restartGame();
                return;
            }
        }
    }

    private void removeFlowersIfPassed() {
        if (!flowers.isEmpty()) {
            Flower flower = flowers.first();
            if (flower.getX() < -Flower.WIDTH) {
                flowers.removeValue(flower, true);
            }
        }
    }

    private void createNewFlower() {
        Flower flower = new Flower();
        flower.setPosition(Constants.WORLD_WIDTH + Flower.WIDTH);
        flowers.add(flower);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void blackFlappyBeeLeaveTheWorld() {
        flappyBee.setPosition(flappyBee.getX()
                , MathUtils.clamp(flappyBee.getY(), 24, Constants.WORLD_HEIGHT));
    }

    private void restartGame() {
        flappyBee.setPosition(Constants.WORLD_WIDTH / 4, Constants.WORLD_HEIGHT / 2);
        flowers.clear();
    }
}

