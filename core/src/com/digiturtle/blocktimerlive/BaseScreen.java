package com.digiturtle.blocktimerlive;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public abstract class BaseScreen extends ScreenAdapter {

	protected static int width;

	private static int height;
	
	private boolean inited = false;
	
	private Stage stage;
	
	private ArrayList<Consumer<String>> eventListeners = new ArrayList<>();
	
	private Consumer<Class<? extends BaseScreen>> changeScreen;
	
	private Supplier<Data> retrieveData;
	
	private BiConsumer<String, Consumer<FileHandle>> fileChooser;
	
	private boolean queuePrepare = false;
	
	public abstract void init();
	
	public abstract void prepare();
	
	public void leave() {
		
	}
	
	public void tryPrepare() {
		if (inited) {
			prepare();
		} else {
			queuePrepare = true;
		}
	}
	
	public void toScreen(Class<? extends BaseScreen> screenType) {
		changeScreen.accept(screenType);
	}
	
	public Data getData() {
		return retrieveData.get();
	}
	
	public void openFileChooser(String title, Consumer<FileHandle> fileChosen) {
		fileChooser.accept(title, fileChosen);
	}
	
	public void create(Consumer<Class<? extends BaseScreen>> nextScreen, Supplier<Data> getData, BiConsumer<String, Consumer<FileHandle>> fileChooser) {
		stage = new Stage();
		changeScreen = nextScreen;
		retrieveData = getData;
		this.fileChooser = fileChooser;
	}
	
	public void tick(float delta) {
		
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(Theme.BACKGROUND.r, Theme.BACKGROUND.g, Theme.BACKGROUND.b, Theme.BACKGROUND.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!inited && width > 0 && height > 0) {
			init();
			inited = true;
			if (queuePrepare) {
				prepare();
				queuePrepare = false;
			}
		}
		getStage().act(delta);
		getStage().draw();
		tick(delta);
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void registerListener(Consumer<String> listener) {
		eventListeners.add(listener);
	}
	
	public void resize(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
	}
	
	public Drawable createDrawable(Color fill) {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(fill);
		pixmap.fill();
		return new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
	}
	
	public Label createLabel(String text, final BitmapFont textFont, final Color textColor, Rectangle region) {
		Label label = new Label(text, new Label.LabelStyle() {
			{
				font = textFont;
				fontColor = textColor;
			}
		});
		if (region != null) {
			scaleToScreen(region);
			adjustToPad(region, 0);
			label.setBounds(region.x, region.y, region.width, region.height);
		}
		return label;
	}
	
	public Button createButton(String label, final BitmapFont buttonFont, final String eventId, Rectangle region, int padding, final Color backgroundColor) {
		Button button = new TextButton(label, new TextButton.TextButtonStyle() {
			{
				up = createDrawable(backgroundColor);
				font = buttonFont;
			}
		});
		if (region != null) {
			scaleToScreen(region);
			adjustToPad(region, padding);
			button.setBounds(region.x, region.y, region.width, region.height);
		}
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				for (int i = 0; i < eventListeners.size(); i++) {
					eventListeners.get(i).accept(eventId);
				}
			}
		});
		return button;
	}
	
	public void adjustToPad(Rectangle rectangle, int padding) {
		rectangle.x += padding;
		rectangle.y += padding;
		rectangle.width -= 2 * padding;
		rectangle.height -= 2 * padding;
	}
	
	public void scaleToScreen(Rectangle rectangle) {
		rectangle.x *= width;
		rectangle.y *= height;
		rectangle.width *= width;
		rectangle.height *= height;
	}
	
	public <T extends Actor, D> ScrollableList<T, D> createScrollableList(Class<T> type, Class<D> data, Rectangle regionBounds, int padding, float itemHeight, ActorFactory<D, Actors<T>> factory) {
		regionBounds.x *= width;
		regionBounds.width *= width;
		regionBounds.y *= height;
		regionBounds.height *= height;
		itemHeight *= height;
		ScrollPaneStyle paneStyle = new ScrollPaneStyle();
	    paneStyle.background = createDrawable(new Color(0, 0, 0, 0));
	    paneStyle.vScrollKnob = createDrawable(Color.WHITE);
	    paneStyle.hScroll = paneStyle.hScrollKnob = paneStyle.vScroll = paneStyle.vScrollKnob;
	    Table container = new Table();
	    Table table = new Table();
	    ScrollPane pane = new ScrollPane(table, paneStyle);
	    container.setBounds(regionBounds.x, regionBounds.y, regionBounds.width, regionBounds.height);
	    table.setBounds(regionBounds.x + padding / 2, regionBounds.y + padding / 2, regionBounds.width - padding, regionBounds.height - padding);
	    pane.setBounds(regionBounds.x + padding / 2, regionBounds.y + padding / 2, regionBounds.width - padding, regionBounds.height - padding);
	    container.add(pane).width(regionBounds.width);
	    table.align(Align.top);
	    return new ScrollableList<T, D>(pane, itemHeight, padding, factory);
	}
	
}
