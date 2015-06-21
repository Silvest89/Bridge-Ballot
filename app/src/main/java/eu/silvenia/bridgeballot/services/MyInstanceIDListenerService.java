package eu.silvenia.bridgeballot.services;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

import eu.silvenia.bridgeballot.Account;

/**
 * Class which regulates token refresh (a token is the unique id of an android device, used for GCM)
 */

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onCreate(){

    }

    /**
     * Method which updates the token when prompted
     */
    @Override
    public void onTokenRefresh(){
        InstanceID instanceID = InstanceID.getInstance(this);
        String gcmToken = null;
        try {
            gcmToken = instanceID.getToken("500415068393",
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(gcmToken);
        if(gcmToken != null) {
            Account.setToken(gcmToken);
            //Config.setGcmToken(gcmToken);
        }
    }

}
