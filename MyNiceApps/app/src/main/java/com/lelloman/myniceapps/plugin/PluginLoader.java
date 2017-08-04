package com.lelloman.myniceapps.plugin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import com.lelloman.androidplugins.AbstractPlugin;

import java.io.File;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;


public class PluginLoader {

	public LoadedPlugin loadPlugin(Context context, String apkPath) {
		try {
			File codeCacheDir;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				codeCacheDir = context.getCodeCacheDir();
			} else {
				codeCacheDir = context.getCacheDir();
			}
			PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(apkPath,0);
			DexClassLoader dexClassLoader = new DexClassLoader(apkPath, codeCacheDir.getAbsolutePath(), null, context.getClassLoader());
			PluginContext pluginContext = new PluginContext(context, apkPath, dexClassLoader);

			Class pluginClass = dexClassLoader.loadClass(packageInfo.packageName + ".Plugin");
			Constructor constructor = pluginClass.getConstructor(Context.class);

			AbstractPlugin plugin = (AbstractPlugin) constructor.newInstance(pluginContext);

			return new LoadedPlugin(pluginContext, plugin, apkPath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
