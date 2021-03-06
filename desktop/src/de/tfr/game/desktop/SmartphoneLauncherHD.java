package de.tfr.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.tfr.game.HotClack;
import de.tfr.game.config.AndroidConfig;

/**
 * Runs the game with smartphone aspect ratio in FullHD resolution (1080 x 1920).
 * ## Run configuration setup:
 * Set the working directory to `\hitklack\android\assets`
 *
 * @author Tobse4Git@gmail.com
 * @see SmartphoneLauncher
 */
public class SmartphoneLauncherHD {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1080;
		config.height = 1920;
		config.samples = 2;
        new LwjglApplication(new HotClack(new AndroidConfig()), config);
    }
}
