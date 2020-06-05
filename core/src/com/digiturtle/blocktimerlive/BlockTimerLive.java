package com.digiturtle.blocktimerlive;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class BlockTimerLive extends Game {
	
	private ManageScreen manageScreen = new ManageScreen();
	private RunScreen runScreen = new RunScreen();
	private ImportScreen importScreen = new ImportScreen();
	
	@Override
	public void create () {
		manageScreen.create();
		Theme.create();
		Gdx.input.setInputProcessor(manageScreen.getStage());
		setScreen(manageScreen);
	}
	
	@Override
	public void dispose () {
	
	}
	
}
