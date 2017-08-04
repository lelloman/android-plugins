package com.lelloman.myniceapps;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;


import com.lelloman.myniceapps.plugin.LoadedPlugin;
import com.lelloman.myniceapps.plugin.PluginLoader;

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
		PluginLoader pluginLoader = new PluginLoader();

		for (String fileName : plugins) {
			LoadedPlugin plugin = pluginLoader.loadPlugin(this, new File(getFilesDir(), fileName).getAbsolutePath());
			if (plugin != null) {
				View view = plugin.createHomeCard();

				if (view != null) {
					CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.card_plugin_home, null);
					cardView.addView(view);

					pluginsContainer.addView(cardView);
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
					if (outFile.exists()) {
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

}
