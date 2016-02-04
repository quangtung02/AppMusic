package app.alone.com.appmusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import app.alone.com.appmusic.R;
import app.alone.com.appmusic.screen.home.FontCacheUtil;


public class TextViewFont extends TextView{

	public TextViewFont(Context context) {
		super(context);
		init(null);
	}

	public TextViewFont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	public TextViewFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public void init(AttributeSet attrs) {
		if(attrs!=null){
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontView);
			final String fontName = a.getString(R.styleable.FontView_font);
			if(TextUtils.isEmpty(fontName))
				setTypeface(FontCacheUtil.get(FontCacheUtil.DEFAULT_ROBOTO_REGULAR, getContext()));
			else
				setTypeface(FontCacheUtil.get(fontName, getContext()));

			setTypeface(FontCacheUtil.get(FontCacheUtil.DEFAULT_UTM_ANDROGYNE, getContext()));
		}else{
			setTypeface(FontCacheUtil.get(FontCacheUtil.DEFAULT_ROBOTO_REGULAR, getContext()));
		}
	}
}
