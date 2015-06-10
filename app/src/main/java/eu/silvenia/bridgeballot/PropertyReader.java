package eu.silvenia.bridgeballot;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Jesse on 10-6-2015.
 */
public class PropertyReader {
    private Context context;
    private Properties properties;

    public PropertyReader(Context context) {
        this.context = context;
        properties = new Properties();
    }

    public Properties getProperties(String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);

        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.toString());
        }
        return properties;
    }
}
