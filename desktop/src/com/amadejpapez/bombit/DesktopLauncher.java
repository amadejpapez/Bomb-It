package com.amadejpapez.bombit;

import com.amadejpapez.bombit.config.GameConfig;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Bomb It 4");
		config.setWindowedMode((int) GameConfig.WIDTH, (int) GameConfig.HEIGHT);
		new Lwjgl3Application(new BombIt(), config);
	}
}
