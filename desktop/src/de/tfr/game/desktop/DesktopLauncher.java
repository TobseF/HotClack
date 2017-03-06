package de.tfr.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.tfr.game.HotClack;
import de.tfr.game.config.AndroidConfig;
import de.tfr.game.config.ComputerConfig;

/**
 * ## Run configuration setup:
 * Set the working directory to `\hitklack\android\assets`
 *
 * @author Tobse4Git@gmail.com
 */
public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.samples = 3;
		//config.fullscreen = true;
		new LwjglApplication(new HotClack(new ComputerConfig()), config);
	}
}
