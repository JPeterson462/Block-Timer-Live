package com.digiturtle.blocktimerlive;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import net.spookygames.gdx.nativefilechooser.NativeFileChooser;
import net.spookygames.gdx.nativefilechooser.NativeFileChooserCallback;
import net.spookygames.gdx.nativefilechooser.NativeFileChooserConfiguration;

public class BlockTimerLive extends Game {
	
	private ManageScreen manageScreen = new ManageScreen();
	private RunScreen runScreen = new RunScreen();
	private ImportScreen importScreen = new ImportScreen();
	private SplashScreen splashScreen = new SplashScreen();
	
	private BaseScreen activeScreen;
	
	private NativeFileChooser nativeFileChooser;
	
	private Data data = new Data();
	
	private FileHandle savedTimers;
	
	public BlockTimerLive(NativeFileChooser nativeFileChooser) {
		this.nativeFileChooser = nativeFileChooser;
	}
	
	public void selectFiles(String chooserTitle, final Consumer<FileHandle> fileChosen) {
		NativeFileChooserConfiguration configuration = new NativeFileChooserConfiguration();
		configuration.title = "";
		nativeFileChooser.chooseFile(configuration, new NativeFileChooserCallback() {
			@Override
			public void onFileChosen(FileHandle file) {
				fileChosen.accept(file);
			}
			@Override
			public void onCancellation() {
				fileChosen.accept(null);
			}
			@Override
			public void onError(Exception exception) {
				System.out.println("Exception: " + exception.getMessage());
			}
		});
	}
	
	private void toScreen(BaseScreen screen) {
		Gdx.input.setInputProcessor(screen.getStage());
		screen.tryPrepare();
		setScreen(screen);
		activeScreen = screen;
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
		case "SplashScreen":
			toScreen(splashScreen);
			return;
		}
	}
	
	public void loadTimersFromJson() {
		savedTimers = Gdx.files.local("data/timers.json");
		if (savedTimers.exists()) {
			data.getTimers().addAll(TimerReader.readAllFromFile(savedTimers));
		}
	}
	
	@Override
	public void create () {
		//data.getTimers().add(TimerReader.readFromFile(Gdx.files.internal("sample_timer.json")));
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
		BiConsumer<String, Consumer<FileHandle>> openFileChooser = new BiConsumer<String, Consumer<FileHandle>>() {
			@Override
			public void accept(String name, Consumer<FileHandle> consumer) {
				selectFiles(name, consumer);
			}
		};
		splashScreen.create(screenChange, getData, openFileChooser);
		manageScreen.create(screenChange, getData, openFileChooser);
		runScreen.create(screenChange, getData, openFileChooser);
		importScreen.create(screenChange, getData, openFileChooser);
		splashScreen.setAction(new Runnable() {
			public void run() {
				Theme.create();
				loadTimersFromJson();
			}
		});
		changeScreen(SplashScreen.class);
	}
	
	@Override
	public void dispose () {
		activeScreen.leave();
		if (!savedTimers.exists()) {
			try {
				savedTimers.file().getParentFile().mkdirs();
				savedTimers.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			TimerWriter.writeToFile(savedTimers, data.getTimers());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
