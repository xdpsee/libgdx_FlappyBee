package com.zhenhui.libgdx.flappybee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Background implements Disposable {

    private Texture texture;

    public Background() {
        texture = new Texture(Gdx.files.internal("background.png"));
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public void draw(SpriteBatch batch) {

        batch.disableBlending();

        batch.draw(texture, 0, 0);

        batch.enableBlending();
    }

}
