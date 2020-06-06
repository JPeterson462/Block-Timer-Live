package com.digiturtle.blocktimerlive;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.digiturtle.blocktimerlive.BlockTimerLive;

import net.spookygames.gdx.nativefilechooser.android;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BlockTimerLive(new AndroidFileChooser(this)), config);
	}
}
