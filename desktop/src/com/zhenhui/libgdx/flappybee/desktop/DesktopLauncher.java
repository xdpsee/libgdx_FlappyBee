package com.zhenhui.libgdx.flappybee.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.zhenhui.libgdx.flappybee.FlappyBeeGame;

public class DesktopLauncher {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Flappy Bee");
        config.setWindowedMode(240, 320);

        new Lwjgl3Application(new FlappyBeeGame(), config);
    }
}
