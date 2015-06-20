package eu.silvenia.bridgeballot.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import eu.silvenia.bridgeballot.activity.Main;
import eu.silvenia.bridgeballot.R;

/**
 * Class for the service which regulates notification via Google Cloud Messaging (GCM)
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

    /**
     * Method which builds a new notofication (sets the title and content)
     * @param context
     */
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

        myIntent.setClass(myContext, Main.class);
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
