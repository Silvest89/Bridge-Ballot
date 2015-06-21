package eu.silvenia.bridgeballot.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;

/**
 * Class which handles the sending of the token to the server
 */
public class MyIntentService extends IntentService {

    public static ActivityHandler handler;

    public MyIntentService(){
        super("MyIntent");
    }

    /**
     * Method which sets the token to the currently logged in account
     * @param intent
     */
    public void onHandleIntent(Intent intent){
        try {
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            InstanceID instanceID = InstanceID.getInstance(this);
            String gcmToken = instanceID.getToken("500415068393",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("BridgeList-Ballot", "GCM Registration Token: " + gcmToken);

            Account.setToken(gcmToken);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
