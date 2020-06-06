package com.digiturtle.blocktimerlive;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.digiturtle.blocktimerlive.Timer.Step;
import com.digiturtle.blocktimerlive.Timer.Interval;

public class TimerReader {
	
	private static int timeToSeconds(String time) {
		int seconds = 0;;
		String[] parts = time.split(":");
		int units = 1;
		for (int i = parts.length - 1; i >= 0; i--) {
			seconds += units * Integer.parseInt(parts[i]);
			units *= 60;
		}
		return seconds;
	}
	
	private static Interval readInterval(JsonValue object) {
		String intervalName = object.getString("name");
		String intervalTime = object.getString("time");
		return new Interval(intervalName, timeToSeconds(intervalTime));
	}
	
	private static Step readStep(JsonValue object) {
		String stepTitle = object.getString("title");
		JsonValue intervals = object.get("intervals");
		Interval[] intervalSet = new Interval[intervals.size];
		int i = 0;
		for (JsonValue entry = intervals.child; entry != null; entry = entry.next) {
			intervalSet[i++] = readInterval(entry);
		}
		return new Step(stepTitle, intervalSet);
	}
	
	public static ArrayList<Timer> readAllFromFile(FileHandle handle) {
		JsonReader reader = new JsonReader();
		JsonValue timer = reader.parse(handle);
		ArrayList<Timer> timers = new ArrayList<>();
		for (JsonValue entry = timer.child; entry != null; entry = entry.next) {
			timers.add(readFromJson(entry));
		}
		return timers;
	}
	
	public static Timer readFromFile(FileHandle handle) {
		JsonReader reader = new JsonReader();
		JsonValue timer = reader.parse(handle);
		return readFromJson(timer);
	}
	
	public static Timer readFromJson(JsonValue timer) {
		String timerName = timer.getString("name");
		String timerGuid = timer.getString("guid");
		JsonValue steps = timer.get("steps");
		Step[] stepSet = new Step[steps.size];
		int i = 0;
		for (JsonValue entry = steps.child; entry != null; entry = entry.next) {
			stepSet[i++] = readStep(entry);
		}
		return new Timer(timerName, UUID.fromString(timerGuid), stepSet);
	}

}
