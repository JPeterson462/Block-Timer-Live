package com.digiturtle.blocktimerlive;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Theme {
	
	public static final Color BACKGROUND = new Color(.3f, .3f, .3f, 1f);
	
	public static final Color GREEN = new Color(0, .72f, 0, 1f);
	public static final Color RED = new Color(.72f, 0, 0, 1f);
	public static final Color ORANGE = new Color(.72f, .72f, 0, 1f);
	public static final Color LIGHT_GREY = new Color(.6f, .6f, .6f, 1f);
	
	public static final String FONT_AWESOME_TRASH = getUnicodeCharacter(61460);
	public static final String FONT_AWESOME_PLAY = getUnicodeCharacter(61515);
	public static final String FONT_AWESOME_PAUSE = getUnicodeCharacter(61516);
	public static final String FONT_AWESOME_STOP = getUnicodeCharacter(61517);
	public static final String FONT_AWESOME_REWIND = getUnicodeCharacter(61514);
	public static final String FONT_AWESOME_FAST_FORWARD = getUnicodeCharacter(61518);
	public static final String FONT_AWESOME_ADD = getUnicodeCharacter(61525);
	public static final String FONT_AWESOME_CANCEL = getUnicodeCharacter(61527);
	public static final String FONT_AWESOME_BOX = getUnicodeCharacter(61831);
	public static final String FONT_AWESOME_FOLDER = getUnicodeCharacter(61563);
	public static final String FONT_AWESOME_OPEN_FOLDER = getUnicodeCharacter(61564);
	
	public static BitmapFont FONT_MD;
	public static BitmapFont FONT_XL;
	
	public static BitmapFont FONT_AWESOME;
	
	public static void create() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
		FONT_MD = generator.generateFont(new FreeTypeFontParameter() {
			{
				size = Gdx.graphics.getHeight() / 40;
			}
		});
		FONT_XL = generator.generateFont(new FreeTypeFontParameter() {
			{
				size = Gdx.graphics.getHeight() / 10;
			}
		});
		generator.dispose();
		
//		generator = new FreeTypeFontGenerator(Gdx.files.internal("fontawesome-webfont.ttf"));
//		FONT_AWESOME = generator.generateFont(new FreeTypeFontParameter() {
//			{
//				size = 16;
//			}
//		});
//		generator.dispose();
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			FONT_AWESOME = new BitmapFont(Gdx.files.internal("FontAwesomeDesktop_v2.0.fnt"));
		}
		else {
			FONT_AWESOME = new BitmapFont(Gdx.files.internal("FontAwesomeAndroid_v2.0.fnt"));
		}
	}
	
	public static String getUnicodeCharacter(int unicode) {
		return new String(Character.toChars(unicode));
	}

}
