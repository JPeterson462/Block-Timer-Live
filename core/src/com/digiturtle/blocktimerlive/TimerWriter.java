package com.digiturtle.blocktimerlive;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.ValueType;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.digiturtle.blocktimerlive.Timer.Interval;
import com.digiturtle.blocktimerlive.Timer.Step;

public class TimerWriter {
	
	private static JsonValue intervalToJson(Interval interval) {
		JsonValue value = new JsonValue(ValueType.object);
		value.addChild("name", new JsonValue(interval.getName()));
		value.addChild("time", new JsonValue(Integer.toString(interval.getNumberOfSeconds())));
		return value;
	}
	
	private static JsonValue stepToJson(Step step) {
		JsonValue value = new JsonValue(ValueType.object);
		value.addChild("title", new JsonValue(step.getTitle()));
		JsonValue intervals = new JsonValue(ValueType.array);
		for (int i = 0; i < step.getIntervals().length; i++) {
			intervals.addChild(intervalToJson(step.getIntervals()[i]));
		}
		value.addChild("intervals", intervals);
		return value;
	}
	
	private static JsonValue timerToJson(Timer timer) {
		JsonValue value = new JsonValue(ValueType.object);
		value.addChild("name", new JsonValue(timer.getName()));
		value.addChild("guid", new JsonValue(timer.getGuid().toString()));
		JsonValue steps = new JsonValue(ValueType.array);
		for (int i = 0; i < timer.getSteps().length; i++) {
			steps.addChild(stepToJson(timer.getSteps()[i]));
		}
		value.addChild("steps", steps);
		return value;
	}
	
	private static JsonValue timerListToJson(ArrayList<Timer> timers) {
		JsonValue value = new JsonValue(ValueType.array);
		for (int i = 0; i < timers.size(); i++) {
			value.addChild(timerToJson(timers.get(i)));
		}
		return value;
	}
	
	public static void writeToFile(FileHandle handle, ArrayList<Timer> timers) throws IOException {
		JsonWriter writer = new JsonWriter(new BufferedWriter(new FileWriter(handle.file())));
		JsonValue timerListJson = timerListToJson(timers);
		writer.write(timerListJson.toJson(OutputType.json));
		writer.close();
	}

}
