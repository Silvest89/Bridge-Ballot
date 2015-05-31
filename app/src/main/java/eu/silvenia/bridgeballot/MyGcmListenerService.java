package eu.silvenia.bridgeballot;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;

/**
 * Created by Jesse on 29-5-2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    private final String TAG = "BridgeBallot";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        //sendNotification(message);

}
// TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(token);

// You should store a boolean that indicates whether the generated token has been
// sent to your server. If the boolean is false, send the token to your server,
// otherwise your server should have already received the token.
        //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();

}
