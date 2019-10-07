package com.zhenhui.libgdx.flappybee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    private Music music;

    private Sound sound;

    //
    private FlappyBee flappyBee;

    private Array<Flower> flowers = new Array<>();

    private Background background;

    private Texture flowerTexure;

    private Texture stemTexure;

    private int score;

    public GameScreen() {
        camera = new OrthographicCamera();
        camera.position.set(new Vector2(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2), 0);
        camera.update();

        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        flappyBee = new FlappyBee(0, 0);
        flappyBee.setPosition(Constants.WORLD_WIDTH / 4, Constants.WORLD_HEIGHT / 2);

        background = new Background();
        flowerTexure = new Texture(Gdx.files.internal("flower.png"));
        stemTexure = new Texture(Gdx.files.internal("stem.png"));

        music = Gdx.audio.newMusic(Gdx.files.internal("11638.wav"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        sound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        background.dispose();
        flowerTexure.dispose();
        stemTexure.dispose();
        flappyBee.dispose();

        for (Flower flower : flowers) {
            flower.dispose();
        }

        music.stop();
        music.dispose();
        sound.dispose();
    }

    @Override
    public void render(float delta) {

        update(delta);

        clearScreen();

        //drawDebug();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();

        background.draw(batch);

        for (Flower flower : flowers) {
            flower.draw(batch);
        }

        flappyBee.draw(batch);

        batch.end();
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Flower flower : flowers) {
            flower.drawDebug(shapeRenderer);
        }

        flappyBee.drawDebug(shapeRenderer);

        shapeRenderer.end();
    }

    private void update(float delta) {

        flappyBee.update(delta);

        updateFlowers(delta);

        updateScore();

        checkForCollision();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            sound.play(0.5f);
            flappyBee.flayUp();
        }
    }

    private void updateFlowers(float delta) {

        for (Flower flower : flowers) {
            flower.update(delta);
        }

        checkIfNewFlowerIsNeeded();
        removeFlowersIfPassed();
    }

    private void updateScore() {

        Flower flower = flowers.first();
        if (flower.getX() < flappyBee.getX()) {
            score += 10;
        }
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
            Flower firstFlower = flowers.first();
            if (firstFlower.getX() < -Flower.WIDTH) {
                flowers.removeValue(firstFlower, true);
            }
        }
    }

    private void createNewFlower() {
        Flower flower = new Flower(flowerTexure, stemTexure);
        flower.setPosition(Constants.WORLD_WIDTH + Flower.WIDTH);
        flowers.add(flower);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void restartGame() {
        flowers.clear();
        flappyBee.setPosition(Constants.WORLD_WIDTH / 4, Constants.WORLD_HEIGHT / 2);
    }

    @Override
    public void pause () {
        music.pause();
    }

    @Override
    public void resume () {
        music.play();
    }

}

