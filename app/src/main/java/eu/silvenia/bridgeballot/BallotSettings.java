package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;


public class BallotSettings extends Activity {

    private boolean notificationsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballot_settings);
    }

    public void onNotification(View v){
        CheckedTextView notifications = (CheckedTextView) findViewById(R.id.notifications_setting);
        if (notifications.isChecked()){
            notifications.setChecked(false);
        }
        else {
            notifications.setChecked(true);
        }

        notificationsOn = notifications.isChecked();
        Toast.makeText(this, String.valueOf(notificationsOn), Toast.LENGTH_SHORT).show();
    }

    public boolean getNotificationsOn(){
        return notificationsOn;
    }



}
