package com.digiturtle.blocktimerlive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SplashScreen extends BaseScreen {
	
	private Runnable action;
	
	private int frames;

	public void setAction(Runnable runnable) {
		action = runnable;
	}
	
	public void tick(float delta) {
		if (frames++ == 2) {
			action.run();
			toScreen(ManageScreen.class);
		}
	}
	
	@Override
	public void init() {
		frames = 0;
		Image image = new Image(new TextureRegionDrawable(new Texture(Gdx.files.internal("FontAwesomeSplash_v1.0_0.png"))));
		// from .fnt: 431x503 * .5 = 215x251
		int width = 215, height = 251;
		image.setBounds((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);
		getStage().addActor(image);
	}

	@Override
	public void prepare() {
		
	}

}
