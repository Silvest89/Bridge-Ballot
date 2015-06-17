package eu.silvenia.bridgeballot;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by kevin on 15-6-15.
 */
public class Config {

    private static String notification = "";
    private static String gcmToken = "";
    private static Properties prop = new Properties();
    public static void readConfig(Context context){

        try {

            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("BridgeBallot.properties");
            prop.load(inputStream);

            notification = prop.getProperty("notification");
            gcmToken = prop.getProperty("gcmtoken");

        } catch (IOException e) {
            Log.e("AssetsPropertyReader", e.toString());
        }
    }

    public static String getNotification() {
        return notification;
    }

    public static void setNotification(String notification) {
        Config.notification = notification;
        prop.setProperty("notification", notification );
    }

    public static String getGcmToken() {
        return gcmToken;
    }

    public static void setGcmToken(String gcmToken) {
        Config.gcmToken = gcmToken;
        //prop.setProperty("gcmtoken", gcmToken);
    }
}
