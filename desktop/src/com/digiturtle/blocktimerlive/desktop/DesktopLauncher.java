package com.digiturtle.blocktimerlive.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.digiturtle.blocktimerlive.BlockTimerLive;

import net.spookygames.gdx.nativefilechooser.desktop.DesktopFileChooser;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 320;
		config.height = 640;
		BlockTimerLive timer = new BlockTimerLive(new DesktopFileChooser());
		new LwjglApplication(timer, config);
		timer.save();
	}
}
