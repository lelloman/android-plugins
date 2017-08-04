package com.lelloman.myniceapps.plugin;

import android.view.View;


import com.lelloman.androidplugins.AbstractPlugin;

import java.lang.reflect.InvocationTargetException;


public class LoadedPlugin {

	final private PluginContext pluginContext;
	final private AbstractPlugin plugin;

	LoadedPlugin(PluginContext context, AbstractPlugin plugin, String apkPath) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		this.pluginContext = context;
		this.plugin = plugin;

	}

	public View createHomeCard(){
		return plugin.createHomeCard();
	}

}
