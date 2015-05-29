package eu.silvenia.bridgeballot;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Jesse on 29-5-2015.
 */
public class MyGcmListenerService extends GcmListenerService {
    private final String TAG = "BridgeBallot";

    public void onHandleIntent(Intent i){
        try {
        InstanceID instanceID = InstanceID.getInstance(this);
        String token = instanceID.getToken("bridge-ballot-6d80",
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        Log.i(TAG, "GCM Registration Token: " + token);

// TODO: Implement this method to send any registration to your app's servers.
        //sendRegistrationToServer(token);

// You should store a boolean that indicates whether the generated token has been
// sent to your server. If the boolean is false, send the token to your server,
// otherwise your server should have already received the token.
        //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
    }catch (IOException e){

    }
}
}
