package eu.silvenia.bridgeballot;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class MyIntentService extends IntentService {

    public MyIntentService(){
        super("MyIntent");
    }
    public void onHandleIntent(Intent intent){
        System.out.println("test1");

        try {
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken("500415068393",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("Bridge-Ballot", "GCM Registration Token: " + token);
            System.out.println(token);
            // TODO: Implement this method to send any registration to your app's servers.
            //sendRegistrationToServer(token);

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
