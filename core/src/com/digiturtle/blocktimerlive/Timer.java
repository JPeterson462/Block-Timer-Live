package com.digiturtle.blocktimerlive;

import java.util.UUID;

public class Timer {
	
	public static class Step {
		
		private String title;
		
		private Interval[] intervals;
		
		public Step(String title, Interval[] intervals) {
			this.title = title;
			this.intervals = intervals;
		}
		
		public String getTitle() {
			return title;
		}
		
		public Interval[] getIntervals() {
			return intervals;
		}
		
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append("Step: ").append(title).append("\n");
			for (int i = 0; i < intervals.length; i++) {
				result.append('\t').append(intervals[i].toString()).append('\n');
			}
			return result.toString();
		}
		
	}
	
	public static class Interval {
		
		private String name;
		
		private int numberSeconds;
		
		public Interval(String name, int numberSeconds) {
			this.name = name;
			this.numberSeconds = numberSeconds;
		}
		
		public String getName() {
			return name;
		}
		
		public int getNumberOfSeconds() {
			return numberSeconds;
		}
		
		public String toString() {
			return "Interval[" + name + ", " + numberSeconds + "]";
		}
		
	}
	
	private String name;
	
	private UUID guid;
	
	private Step[] steps;
	
	public Timer(String name, UUID guid, Step[] steps) {
		this.name = name;
		this.guid = guid;
		this.steps = steps;
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getGuid() {
		return guid;
	}
	
	public Step[] getSteps() {
		return steps;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Timer: ").append(name).append(' ').append(guid.toString()).append('\n');
		for (int i = 0; i < steps.length; i++) {
			result.append(steps[i].toString());
		}
		return result.toString();
	}

}
