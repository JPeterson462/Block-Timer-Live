package com.digiturtle.blocktimerlive;

import java.util.function.Consumer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.digiturtle.blocktimerlive.Timer.Interval;
import com.digiturtle.blocktimerlive.Timer.Step;

public class RunScreen extends BaseScreen {
	
	private Button playPause;
	
	private boolean isPaused = true;
	
	private Drawable orange, green;
	
	private Label timerName, stepName, intervalName, timeLeft;
	
	private Timer timer;
	
	private Stopwatch stopwatch = new Stopwatch();
	
	private Poll poll = new Poll();
	
	private class Poll {
		int milestone = 0;
		int seconds = -1;
	}
	
	private enum Action {
		PLAY, PAUSE, STOP, SKIP_BACK, SKIP_FORWARD
	}
	
	private Milestone[] milestones;
	
	private class Milestone {
		private int markSeconds;
		private String[] name;
		Milestone(int markSeconds, String[] name) {
			this.markSeconds = markSeconds;
			this.name = name;
		}
	}
	
	private class Stopwatch {
		
		private float time;
		private boolean active = false;
		
		public void advance(float delta) {
			if (active) {
				time += delta;
			}
		}
		
		public int seconds() {
			return (int) time;
		}
		
	}
	
	private int getMilestone(int seconds) {
		for (int i = 0; i < milestones.length; i++) {
			if (seconds <= milestones[i].markSeconds) {
				return i - 1;
			}
		}
		return milestones.length;
	}
	
	public void tick(float delta) {
		stopwatch.advance(delta);
		int seconds = stopwatch.seconds();
		if (poll.seconds != seconds) {
			int milestone = getMilestone(seconds);
			if (milestone == milestones.length) {
				// Done!
				toScreen(ManageScreen.class);
				return;
			}
			if (milestone != poll.milestone) {
				Milestone m = milestones[milestone];
				stepName.setText(m.name[0]);
				intervalName.setText(m.name[1]);
				poll.milestone = milestone;	
			}
			poll.seconds = seconds;
			int secondsUntilMilestone = milestones[milestone + 1].markSeconds - poll.seconds + 1;
			int[] time = { (secondsUntilMilestone - secondsUntilMilestone % 60) / 60, secondsUntilMilestone % 60 };
			timeLeft.setText(String.format("%02d", time[0]) + ":" + String.format("%02d", time[1]));
		}		
	}
	
	private void changeToMilestone() {
		if (poll.milestone == milestones.length - 1) {
			toScreen(ManageScreen.class);
			return;
		}
		stepName.setText(milestones[poll.milestone].name[0]);
		intervalName.setText(milestones[poll.milestone].name[1]);
		int secondsUntilMilestone = milestones[poll.milestone + 1].markSeconds - milestones[poll.milestone].markSeconds;
		int[] time = { (secondsUntilMilestone - secondsUntilMilestone % 60) / 60, secondsUntilMilestone % 60 };
		timeLeft.setText(String.format("%02d", time[0]) + ":" + String.format("%02d", time[1]));
	}
	
	private void doAction(Action action) {
		switch (action) {
		case PAUSE:
			stopwatch.active = false;
			break;
		case PLAY:
			stopwatch.active = true;
			break;
		case SKIP_BACK:
			if (poll.milestone > 0) {
				stopwatch.time = milestones[poll.milestone - 1].markSeconds + (stopwatch.time - (int) stopwatch.time);
				poll.seconds = (int) stopwatch.time;
				poll.milestone--;
				changeToMilestone();
			} else {
				System.out.println("Skipping to " + (poll.milestone));
				stopwatch.time = 0;
				changeToMilestone();
			}
			break;
		case SKIP_FORWARD:
			if (poll.milestone < milestones.length - 1) {
				stopwatch.time = milestones[poll.milestone + 1].markSeconds + (stopwatch.time - (int) stopwatch.time);
				poll.seconds = (int) stopwatch.time;
				poll.milestone++;
				changeToMilestone();
			} else {
				doAction(Action.STOP);
			}
			break;
		case STOP:
			stopwatch.time = 0;
			toScreen(ManageScreen.class);
			break;
		}
	}
	
	public void processEvent(String name) {
		switch (name) {
		case "PlayPause":
			if (isPaused) {
				doAction(Action.PLAY);
				((TextButton) playPause).setText(Theme.FONT_AWESOME_PAUSE);
				playPause.getStyle().up = orange;
			} else {
				doAction(Action.PAUSE);
				((TextButton) playPause).setText(Theme.FONT_AWESOME_PLAY);
				playPause.getStyle().up = green;
			}
			isPaused ^= true;
			break;
		case "Stop":
			doAction(Action.STOP);
			break;
		case "SkipBack":
			doAction(Action.SKIP_BACK);
			break;
		case "SkipForward":
			doAction(Action.SKIP_FORWARD);
			break;
		default:
			break;
		}
	}

	@Override
	public void init() {
		isPaused = true;
		orange = createDrawable(Theme.ORANGE);
		green = createDrawable(Theme.GREEN);
		registerListener(new Consumer<String>() {
			@Override
			public void accept(String name) {
				processEvent(name);
			}
		});
		playPause = createButton(Theme.FONT_AWESOME_PLAY, Theme.FONT_AWESOME, "PlayPause", new Rectangle(.3f, 0f, .7f, .1f), 10, Theme.GREEN);
		getStage().addActor(playPause);
		getStage().addActor(createButton(Theme.FONT_AWESOME_STOP, Theme.FONT_AWESOME, "Stop", new Rectangle(0f, 0f, .3f, .1f), 10, Theme.RED));
		getStage().addActor(createButton(Theme.FONT_AWESOME_REWIND, Theme.FONT_AWESOME, "SkipBack", new Rectangle(0f, .9f, .3f, .1f), 10, Theme.LIGHT_GREY));
		getStage().addActor(createButton(Theme.FONT_AWESOME_FAST_FORWARD, Theme.FONT_AWESOME, "SkipForward", new Rectangle(.7f, .9f, .3f, .1f), 10, Theme.LIGHT_GREY));
		timerName = createLabel("", Theme.FONT_MD, Color.WHITE, new Rectangle(.1f, .7f, .8f, .1f));
		stepName = createLabel("", Theme.FONT_MD, Color.WHITE, new Rectangle(.15f, .6f, .8f, .1f));
		intervalName = createLabel("", Theme.FONT_MD, Color.WHITE, new Rectangle(.2f, .5f, .8f, .1f));
		GlyphLayout layout = new GlyphLayout();
		layout.setText(Theme.FONT_XL, "00:00");
		timeLeft = createLabel("", Theme.FONT_XL, Color.WHITE, new Rectangle(.5f - (layout.width / width) / 2, .3f, layout.width, .1f));
		
		getStage().addActor(timerName);
		getStage().addActor(stepName);
		getStage().addActor(intervalName);
		getStage().addActor(timeLeft);
	}

	@Override
	public void prepare() {
		timer = getData().getTimers().get(getData().getSelectedIndex());
		timerName.setText(timer.getName());
		
		int total = 0;
		for (int i = 0; i < timer.getSteps().length; i++) {
			total += timer.getSteps()[i].getIntervals().length;
		}
		milestones = new Milestone[total + 1];
		total = 0;
		int k = 0, j = 0, i = 0;
		for (i = 0; i < timer.getSteps().length; i++) {
			Step step = timer.getSteps()[i];
			for (j = 0; j < step.getIntervals().length; j++, k++) {
				Interval interval = step.getIntervals()[j];
				milestones[k] = new Milestone(total, new String[] { step.getTitle(), interval.getName() });
				total += interval.getNumberOfSeconds();
			}
		}
		milestones[k] = new Milestone(total, new String[] { timer.getSteps()[i - 1].getTitle(), timer.getSteps()[i - 1].getIntervals()[j - 1].getName() });
		poll.milestone = 0;
		poll.seconds = 0;
		stopwatch.time = 0;
		
		stepName.setText(milestones[0].name[0]);
		intervalName.setText(milestones[0].name[1]);
		int secondsUntilMilestone = milestones[1].markSeconds;
		int[] time = { (secondsUntilMilestone - secondsUntilMilestone % 60) / 60, secondsUntilMilestone % 60 };
		timeLeft.setText(String.format("%02d", time[0]) + ":" + String.format("%02d", time[1]));
	}

}
