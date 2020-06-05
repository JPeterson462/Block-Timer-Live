package com.digiturtle.blocktimerlive;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Actors<T extends Actor> {

	private class ActorCell {
		public T _actor;
		public float _percentage;
	}
	
	private ArrayList<ActorCell> cells = new ArrayList<>();
	
	public void addActor(T actor, float percentage) {
		cells.add(new ActorCell() {
			{
				_actor = actor;
				_percentage = percentage;
			}
		});
	}
	
	public Table add(Table table, int padding, float height) {
		for (ActorCell cell : cells) {
			table.add(cell._actor).width(table.getWidth() * cell._percentage).height(height).pad(0, 0, padding, 0);
		}
		table.row();
		return table;
	}
	
}
