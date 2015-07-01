package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;


public class BallotSettings extends Activity {

    private boolean notificationsOn;
    CheckedTextView notifications;

    /**
     * initialisation of variables and interface
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballot_settings);
        notifications = (CheckedTextView) findViewById(R.id.notifications_setting);
        if(Config.getNotification() == "1")
            notifications.setChecked(true);
        else

            notifications.setChecked(false);
    }

    /**
     * checks if a notification is received
     * @param v
     */
    public void onNotification(View v){
        if (notifications.isChecked()){
            notifications.setChecked(false);
            Config.setNotification("0");
        }
        else {
            notifications.setChecked(true);
            Config.setNotification("1");
        }

        notificationsOn = notifications.isChecked();
        Toast.makeText(this, String.valueOf(notificationsOn), Toast.LENGTH_SHORT).show();
    }

    public boolean getNotificationsOn(){
        return notificationsOn;
    }



}
