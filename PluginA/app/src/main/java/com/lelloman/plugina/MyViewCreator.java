package com.lelloman.plugina;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lelloman.androidplugins.AbstractViewCreator;


public class MyViewCreator extends AbstractViewCreator {

	public MyViewCreator(Context context) {
		super(context);
	}

	@Override
	public View createView() {
		TextView textView = new TextView(getContext());
		textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		textView.setText("Hello Plugin A");
		return textView;
	}
}
