package com.lelloman.myniceapps;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lelloman.androidplugins.AbstractViewCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		List<String> plugins = copyPlugins();

		ViewGroup pluginsContainer = (ViewGroup) findViewById(R.id.plugins_container);

		for (String fileName : plugins) {
			LoadedPlugin loadedPlugin = loadPlugin(fileName);
			if (loadedPlugin != null) {
				AbstractViewCreator viewCreator = loadedPlugin.getViewCreator();
				if (viewCreator != null) {
					View view = viewCreator.createView();
					pluginsContainer.addView(view);
				}
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
					if(outFile.exists()){
						outFile.delete();
					}

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

	private LoadedPlugin loadPlugin(String assetsFileName) {
		try {
			String apkPath = new File(getFilesDir(), assetsFileName).getAbsolutePath();
			return new LoadedPlugin(getApplicationContext(), apkPath);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
