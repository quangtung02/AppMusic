/*
 * Author HaiPQ
 * Copyright 2014 Adways
 * Created 9:32:33 AM Dec 3, 2014
 */
package app.alone.com.appmusic.screen.home;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

import app.alone.com.appmusic.utils.L;

public class FontCacheUtil {
	public static final String DEFAULT_ROBOTO_Bold = "Roboto-Bold.ttf";
	public static final String DEFAULT_ROBOTO_REGULAR = "Roboto-Regular.ttf";
	public static final String DEFAULT_UTM_ANDROGYNE  ="UTM_Androgyne.ttf";
	public static final String DEFAULT_UTM_VBANDIT  ="VBANDIT.ttf";
	/** The font cache. */
	private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

	/**
	 * Load DEFAULT Font
	 */
	public static void init(Context context) {
		get(DEFAULT_ROBOTO_REGULAR, context);
	}

	public static Typeface get(String name, Context context) {
		Typeface tf = fontCache.get(name);
		if (tf == null) {
			try {
				tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + name);
				//L.d("Load font from asset: " + name + "");
			} catch (Exception e) {
				L.e("Font's not found " + name + "");
				return null;
			}
			fontCache.put(name, tf);
		}else{
			//L.d("Load font from memory: " + name + "");
		}
		return tf;
	}

}
