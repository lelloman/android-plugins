package com.lelloman.androidplugins;

import android.content.Context;
import android.view.View;

public abstract class AbstractViewCreator {

	private final Context context;

	public AbstractViewCreator(Context context) {
		this.context = context;
	}

	public abstract View createView();

	protected Context getContext() {
		return context;
	}
}
