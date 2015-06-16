package eu.silvenia.bridgeballot;

import android.app.IntentService;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class MyIntentService extends IntentService {

    public static ActivityHandler handler;

    public MyIntentService(){
        super("MyIntent");
    }
    public void onHandleIntent(Intent intent){

        try {
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            InstanceID instanceID = InstanceID.getInstance(this);
            String gcmToken = instanceID.getToken("500415068393",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("Bridge-Ballot", "GCM Registration Token: " + gcmToken);
            //System.out.println(token);

            Account.setToken(gcmToken);

            // Subscribe to topic channels
            //subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
