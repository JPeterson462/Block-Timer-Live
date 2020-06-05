package com.digiturtle.blocktimerlive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Theme {
	
	public static final Color BACKGROUND = new Color(.3f, .3f, .3f, 1f);
	
	public static BitmapFont FONT_MD;
	
	public static void create() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
		FONT_MD = generator.generateFont(new FreeTypeFontParameter() {
			{
				size = 16;
			}
		});
	}

}
