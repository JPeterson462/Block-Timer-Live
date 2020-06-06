package com.digiturtle.blocktimerlive;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ManageScreen extends BaseScreen {
	
	private ScrollableList<Button, Timer> timers;
	
	private Pattern timerEventRegex;

	public void processEvent(String name) {
		Matcher matcher = timerEventRegex.matcher(name);
		if (matcher.matches()) {
			String guidString = matcher.group(1);
			String action = matcher.group(2);
			//System.out.println("Timer action on GUID[" + guidString + "] to " + action);
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
		getStage().addActor(createButton("Import", Theme.FONT_MD, "ImportTimer", new Rectangle(0, 0, 1f, .1f), 10, Color.BLACK));
		timers = createScrollableList(Button.class, Timer.class, new Rectangle(0, .1f, 1f, .9f), 10, .1f, new ActorFactory<Timer, Actors<Button>>() {
			@Override
			public Actors<Button> create(Timer input) {
				return timerToRow(input);
			}
		});
		getStage().addActor(timers.getScrollPane());
	}

	@Override
	public void prepare() {
		timers.getData().clear();
		Data data = getData();
		for (int i = 0; i < data.getTimers().size(); i++) {
			timers.getData().add(data.getTimers().get(i));
		}
		timers.refresh();
	}
	
}
