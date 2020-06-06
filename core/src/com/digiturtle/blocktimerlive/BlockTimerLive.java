package com.digiturtle.blocktimerlive;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

//import net.spookygames.gdx.NativeFileChooser;

public class BlockTimerLive extends Game {
	
	private ManageScreen manageScreen = new ManageScreen();
	private RunScreen runScreen = new RunScreen();
	private ImportScreen importScreen = new ImportScreen();
	
	private Data data = new Data();
	
//	public BlockTimerLive(NativeFileChooser nativeFileChooser) {
//		
//	}
	
	private void toScreen(BaseScreen screen) {
		Gdx.input.setInputProcessor(screen.getStage());
		screen.tryPrepare();
		setScreen(screen);
	}
	
	public void changeScreen(Class<? extends BaseScreen> nextScreen) {
		switch (nextScreen.getSimpleName()) {
		case "ManageScreen":
			toScreen(manageScreen);
			return;
		case "RunScreen":
			toScreen(runScreen);
			return;
		case "ImportScreen":
			toScreen(importScreen);
			return;
		}
	}
	
	@Override
	public void create () {
		data.getTimers().add(TimerReader.readFromFile(Gdx.files.internal("sample_timer.json")));
		
		Consumer<Class<? extends BaseScreen>> screenChange = new Consumer<Class<? extends BaseScreen>>() {
			@Override
			public void accept(Class<? extends BaseScreen> screen) {
				changeScreen(screen);
			}
		};
		Supplier<Data> getData = new Supplier<Data>() {
			@Override
			public Data get() {
				return data;
			}
		};
		manageScreen.create(screenChange, getData);
		runScreen.create(screenChange, getData);
		importScreen.create(screenChange, getData);
		Theme.create();
		changeScreen(ManageScreen.class);
	}
	
	@Override
	public void dispose () {
	
	}
	
}
