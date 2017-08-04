package com.lelloman.plugina;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.lelloman.androidplugins.AbstractPlugin;


public class Plugin extends AbstractPlugin {

	public Plugin(Context context) {
		super(context);
	}

	@Override
	public View createHomeCard() {
		return LayoutInflater.from(getContext()).inflate(R.layout.view_hello_plugin_a, null);
	}
}
