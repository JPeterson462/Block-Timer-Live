package com.digiturtle.blocktimerlive;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ScrollableList<T extends Actor, D> {

	private ArrayList<Actors<T>> items;
	
	private ArrayList<D> data;
	
	private ScrollPane scrollPane;
	
	private float itemHeight;
	
	private int padding;
	
	private ActorFactory<D, Actors<T>> factory;
	
	public ScrollableList(ScrollPane scrollPane, float itemHeight, int padding, ActorFactory<D, Actors<T>> factory) {
		items = new ArrayList<Actors<T>>();
		data = new ArrayList<D>();
		this.scrollPane = scrollPane;
		this.itemHeight = itemHeight;
		this.padding = padding;
		this.factory = factory;
	}
	
	public ArrayList<D> getData() {
		return data;
	}
	
	public ScrollPane getScrollPane() {
		return scrollPane;
	}
	
	public void refresh() {
		items.clear();
		for (D datum : data) {
			items.add(factory.create(datum));
		}
		Table table = (Table) scrollPane.getActor();
		table.setBounds(scrollPane.getX(), scrollPane.getY(), scrollPane.getWidth(), scrollPane.getHeight());
		table.clearChildren();
		for (Actors<T> item : items) {
			item.add(table, padding, itemHeight);
			table.row();
		}
	}

}
