package com.lelloman.myniceapps;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;


import com.lelloman.androidplugins.AbstractViewCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		List<String> plugins = copyPlugins();

		ViewGroup pluginsContainer = (ViewGroup) findViewById(R.id.plugins_container);

		for (String fileName : plugins) {
			AbstractViewCreator viewCreator = extractViewCreatorFromAssetsFile(fileName);
			if (viewCreator != null) {
				pluginsContainer.addView(viewCreator.createView());
			}
		}
	}

	private List<String> copyPlugins() {
		AssetManager assetManager = getAssets();
		List<String> output = new ArrayList<>();
		try {
			String[] files = assetManager.list("");

			for (String filename : files) {
				if (filename.endsWith(".apk")) {
					InputStream in = assetManager.open(filename);
					File outFile = new File(getFilesDir(), filename);

					OutputStream out = new FileOutputStream(outFile);
					copyFile(in, out);
					in.close();
					out.flush();
					out.close();
					output.add(filename);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}


	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	private AbstractViewCreator extractViewCreatorFromAssetsFile(String assetsFileName) {

		try {
			String codeCachePath;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				codeCachePath = getCodeCacheDir().getAbsolutePath();
			} else {
				codeCachePath = getCacheDir().getAbsolutePath();
			}

			String dexPath = new File(getFilesDir(), assetsFileName).getAbsolutePath();

			DexClassLoader dexClassLoader = new DexClassLoader(dexPath, codeCachePath, getFilesDir().getPath(), getClassLoader());

			Class myClass = dexClassLoader.loadClass("com.lelloman." + assetsFileName.replace(".apk", "").toLowerCase() + ".MyViewCreator");
			Constructor constructor = myClass.getConstructor(Context.class);
			AbstractViewCreator creator = (AbstractViewCreator) constructor.newInstance(this);
			return creator;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
