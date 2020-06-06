package com.digiturtle.blocktimerlive;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ImportScreen extends BaseScreen {

	private ScrollableList<Button, Timer> timers;
	
	private Pattern timerEventRegex;
	
	public void processEvent(String name) {
		switch (name) {
		case "OpenFileChooser":
			openFileChooser("Select a Timer JSON to Import", new Consumer<FileHandle>() {
				@Override
				public void accept(FileHandle file) {
					if (file != null) {
						timers.getData().add(TimerReader.readFromFile(file));
						timers.refresh();
					} else {
						System.out.println("Cancelled");
					}
				}
			});
			break;
		case "Import":
			getData().getTimers().addAll(timers.getData());
			toScreen(ManageScreen.class);
			break;
		case "Cancel":
			toScreen(ManageScreen.class);
			break;
		}
		Matcher matcher = timerEventRegex.matcher(name);
		if (matcher.matches()) {
			String guidString = matcher.group(1);
			String action = matcher.group(2);
			switch (action) {
			case "Delete":
				for (int i = 0; i < timers.getData().size(); i++) {
					if (timers.getData().get(i).getGuid().equals(UUID.fromString(guidString))) {
						timers.getData().remove(i);
						break;
					}
				}
				timers.refresh();
				break;
			case "Select":
				int selectedIndex = -1;
				for (int i = 0; i < timers.getData().size(); i++) {
					if (timers.getData().get(i).getGuid().equals(UUID.fromString(guidString))) {
						selectedIndex = i;
						break;
					}
				}
				getData().setSelectedIndex(selectedIndex);
				toScreen(RunScreen.class);
				break;
			default:
				break;
			}
		}
	}

	private Actors<Button> timerToRow(Timer timer) {
		Actors<Button> row = new Actors<Button>();
		row.addActor(createButton(timer.getName(), Theme.FONT_MD, "Timer[" + timer.getGuid().toString() + "].Select", null, 10, Color.DARK_GRAY), .75f);
		row.addActor(createButton(Theme.FONT_AWESOME_TRASH, Theme.FONT_AWESOME, "Timer[" + timer.getGuid().toString() + "].Delete", null, 10, Color.DARK_GRAY), .25f);
		return row;
	}
	
	@Override
	public void init() {
		timerEventRegex = Pattern.compile("Timer\\[([^\\]]*)\\].([\\s\\S]+)");
		registerListener(new Consumer<String>() {
			@Override
			public void accept(String name) {
				processEvent(name);
			}
		});
		getStage().addActor(createButton(Theme.FONT_AWESOME_OPEN_FOLDER, Theme.FONT_AWESOME, "OpenFileChooser", new Rectangle(0, .9f, 1f, .1f), 10, Theme.ORANGE));
		timers = createScrollableList(Button.class, Timer.class, new Rectangle(0, .1f, 1f, .8f), 10, .1f, new ActorFactory<Timer, Actors<Button>>() {
			@Override
			public Actors<Button> create(Timer input) {
				return timerToRow(input);
			}
		});
		getStage().addActor(timers.getScrollPane());
		getStage().addActor(createButton(Theme.FONT_AWESOME_ADD, Theme.FONT_AWESOME, "Import", new Rectangle(.5f, 0f, .5f, .1f), 10, Theme.GREEN));
		getStage().addActor(createButton(Theme.FONT_AWESOME_CANCEL, Theme.FONT_AWESOME, "Cancel", new Rectangle(0f, 0f, .5f, .1f), 10, Theme.RED));
	}

	@Override
	public void prepare() {
		timers.getData().clear();
		timers.refresh();
	}

}
