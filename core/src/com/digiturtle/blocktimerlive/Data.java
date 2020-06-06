package com.digiturtle.blocktimerlive;

import java.util.ArrayList;

public class Data {
	
	private ArrayList<Timer> timers = new ArrayList<>();
	
	private int selectedIndex = -1;
	
	public ArrayList<Timer> getTimers() {
		return timers;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public void setSelectedIndex(int index) {
		selectedIndex = index;
	}

}
