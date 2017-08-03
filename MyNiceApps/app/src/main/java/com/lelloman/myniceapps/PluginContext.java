package com.lelloman.myniceapps;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PluginContext extends ContextWrapper {

	private final AssetManager assetManager;
	private final Resources resources;
	private final LayoutInflater layoutInflater;

	public PluginContext(Context baseContext, String apkPath) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		super(baseContext);

		assetManager = AssetManager.class.newInstance();
		Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
		addAssetPath.invoke(assetManager, apkPath);

		Resources baseResources = baseContext.getResources();
		resources = new Resources(assetManager, baseResources.getDisplayMetrics(), baseResources.getConfiguration()){
			@NonNull
			@Override
			public String getString(@StringRes int id) throws NotFoundException {
				return super.getString(id);
			}

			@NonNull
			@Override
			public CharSequence getText(@StringRes int id) throws NotFoundException {
				return super.getText(id);
			}
		};

		layoutInflater = new PluginLayoutInflater(LayoutInflater.from(baseContext), this);
	}


	@Override
	public Object getSystemService(String name) {
		if(name.equals(LAYOUT_INFLATER_SERVICE)){
			return layoutInflater;
		}
		return super.getSystemService(name);
	}

	@Override
	public Resources getResources() {
		return resources;
	}

	@Override
	public AssetManager getAssets() {
		return assetManager;
	}

	private class PluginLayoutInflater extends LayoutInflater {

		protected PluginLayoutInflater(LayoutInflater original, Context context) {
			super(original, context);
		}

		@Override
		public LayoutInflater cloneInContext(Context newContext) {
			return new PluginLayoutInflater(this, newContext);
		}

		@Override
		public Context getContext() {
			return PluginContext.this;
		}
	}
}
