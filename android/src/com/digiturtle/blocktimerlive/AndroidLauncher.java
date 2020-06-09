package com.digiturtle.blocktimerlive;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.digiturtle.blocktimerlive.BlockTimerLive;

import net.spookygames.gdx.nativefilechooser.android.AndroidFileChooser;

public class AndroidLauncher extends AndroidApplication {
	private BlockTimerLive timer;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		timer = new BlockTimerLive(new AndroidFileChooser(this));
		initialize(timer, config);
	}
	@Override
	protected void onDestroy () {
		timer.save();
	}
}
