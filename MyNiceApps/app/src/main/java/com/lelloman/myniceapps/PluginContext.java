package com.lelloman.myniceapps;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.support.annotation.AnyRes;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;


public class PluginContext extends ContextWrapper {

	private final AssetManager assetManager;
	private final Resources resources;
	private final LayoutInflater layoutInflater;
	private final DexClassLoader dexClassLoader;
	private final Resources.Theme theme;

	public PluginContext(Context baseContext, String apkPath, final DexClassLoader dexClassLoader) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		super(baseContext);
		this.dexClassLoader = dexClassLoader;
		assetManager = AssetManager.class.newInstance();
		Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
		addAssetPath.invoke(assetManager, apkPath);

		Resources baseResources = baseContext.getResources();
		resources = new Resources(assetManager, baseResources.getDisplayMetrics(), baseResources.getConfiguration());
		theme = resources.newTheme();
		theme.setTo(getTheme());

		layoutInflater = new PluginLayoutInflater(LayoutInflater.from(baseContext), this);
	}

	@Override
	public Resources.Theme getTheme() {
		return theme;
	}

	@Override
	public Object getSystemService(String name) {
		if (name.equals(LAYOUT_INFLATER_SERVICE)) {
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

	private class PluginLayoutInflater extends LayoutInflater implements LayoutInflater.Factory2{

		protected PluginLayoutInflater(LayoutInflater original, Context context) {
			super(original, context);
			setFactory2(this);
		}

		@Override
		public LayoutInflater cloneInContext(Context newContext) {
			return new PluginLayoutInflater(this, newContext);
		}

		@Override
		public Context getContext() {
			return PluginContext.this;
		}

		@Override
		public View inflate(@LayoutRes int resource, @Nullable ViewGroup root) {
			return super.inflate(resource, root);
		}

		@Override
		public View onCreateView(String name, Context context, AttributeSet attrs) {
			return onCreateView(null, name, context, attrs);
		}

		@Override
		public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
			Class clazz = null;
			try {
				clazz = getClassLoader().loadClass(name);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if (clazz == null) {
				try {
					clazz = getClassLoader().loadClass("android.widget." + name);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

			if (clazz == null) {
				try {
					clazz = dexClassLoader.loadClass(name);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}

			try {
				Constructor constructor = clazz.getConstructor(Context.class, AttributeSet.class);
				View output = (View) constructor.newInstance(context, attrs);
				return output;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
