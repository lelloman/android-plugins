package com.lelloman.androidplugins;

import android.content.Context;
import android.view.View;

public abstract class AbstractPlugin {

	private final Context context;

	public AbstractPlugin(Context context) {
		this.context = context;
	}

	public abstract View createHomeCard();

	protected Context getContext() {
		return context;
	}
}
