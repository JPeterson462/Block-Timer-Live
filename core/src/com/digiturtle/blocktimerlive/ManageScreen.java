package com.digiturtle.blocktimerlive;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

public class ManageScreen extends BaseScreen {
	
	private ScrollableList<Button, String> timers;

	public void processEvent(String name) {
		System.out.println(name);
	}
	
	private Actors<Button> timerToRow(String timer) {
		Actors<Button> row = new Actors<Button>();
		row.addActor(createButton(timer, "Timer." + timer, null, 10, Color.DARK_GRAY), .75f);
		row.addActor(createButton("Delete", "Timer." + timer + ".Delete", null, 10, Color.DARK_GRAY), .25f);
		return row;
	}
	
	@Override
	public void init() {
		System.out.println("Initializing...");
		registerListener(this::processEvent);
		getStage().addActor(createButton("Import", "ImportTimer", new Rectangle(0, 0, 1f, .1f), 10, Color.BLACK));
		timers = this.createScrollableList(Button.class, String.class, new Rectangle(0, .1f, 1f, .9f), 10, null, this::timerToRow);
		timers.getData().add("Test Timer");
		timers.refresh();
		getStage().addActor(timers.getScrollPane());
	}
	
}
