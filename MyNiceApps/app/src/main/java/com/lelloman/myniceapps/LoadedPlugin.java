package com.lelloman.myniceapps;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import com.lelloman.androidplugins.AbstractViewCreator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

public class LoadedPlugin {

	final private Context baseContext;
	final private PluginContext pluginContext;
	final private String apkPath;
	final private DexClassLoader dexClassLoader;

	final private AbstractViewCreator viewCreator;

	public LoadedPlugin(Context context, String apkPath) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		this.baseContext = context;
		this.apkPath = apkPath;

		File codeCacheDir;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			codeCacheDir = context.getCodeCacheDir();
		} else {
			codeCacheDir = context.getCacheDir();
		}

		dexClassLoader = new DexClassLoader(apkPath, codeCacheDir.getAbsolutePath(), null, context.getClassLoader());
		pluginContext = new PluginContext(baseContext, apkPath, dexClassLoader);

		File apkFile = new File(apkPath);
		String apkName = apkFile.getName().replace(".apk", "").toLowerCase();
		String viewCreatorClassName = "com.lelloman." + apkName + ".MyViewCreator";
		Class myClass = dexClassLoader.loadClass(viewCreatorClassName);
		Constructor constructor = myClass.getConstructor(Context.class);

		viewCreator = (AbstractViewCreator) constructor.newInstance(pluginContext);

	}

	public AbstractViewCreator getViewCreator(){
		return viewCreator;
	}
}
