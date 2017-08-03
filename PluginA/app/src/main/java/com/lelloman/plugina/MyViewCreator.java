package com.lelloman.plugina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lelloman.androidplugins.AbstractViewCreator;


public class MyViewCreator extends AbstractViewCreator {

	public MyViewCreator(Context context) {
		super(context);
	}

	@Override
	public View createView() {
		return LayoutInflater.from(getContext()).inflate(R.layout.view_hello_plugin_a, null);
	}
}
