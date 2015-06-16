package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;

import javax.crypto.spec.GCMParameterSpec;

/**
 * Created by Jesse on 29-5-2015.
 */
public class MyGcmListenerService extends GcmListenerService {
 
    private final String TAG = "BridgeBallot";
    static int notificationId = 1;
    @Override
    public void onMessageReceived(String from, Bundle data){
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        String mContext = message;
        notificationId++;

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
        handleNotification(mContext);

}
    public void handleNotification(String context){
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);


        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("Bridge-Ballot Notification");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Bridge-ballot Notif");
        notification.setContentText(context);
        notification.setAutoCancel(true);


        PendingIntent myPendingIntent;
        Intent myIntent = new Intent();
        Context myContext = getApplicationContext();

        myIntent.setClass(myContext, MainActivity.class);
        //myIntent.putExtra("ID", 1);
        myPendingIntent = PendingIntent.getActivity(myContext, 0, myIntent, 0);
        notification.setContentIntent(myPendingIntent);


        Notification n = notification.build();
        n.flags|= Notification.FLAG_AUTO_CANCEL;
        n.flags |= Notification.FLAG_SHOW_LIGHTS;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        nm.notify(notificationId, n);
    }
// TODO: Implement this method to send any registration to your app's servers.
      //  sendRegistrationToServer(token);


// You should store a boolean that indicates whether the generated token has been
// sent to your server. If the boolean is false, send the token to your server,
// otherwise your server should have already received the token.
        //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();

}
