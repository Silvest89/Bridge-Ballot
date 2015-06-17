package eu.silvenia.bridgeballot;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Johnnie Ho on 11-6-2015.
 */
public class ActivityHandler extends Handler {
    public Activity currentActivity;
    public BallotList currentFragment;
    public ActivityHandler(Activity activity){
        this.currentActivity = activity;
    }

    public ActivityHandler(BallotList fragment){
        this.currentFragment = fragment;
    }

    public void switchActivity(Class<?> cls){
        Intent nextActivity = new Intent(currentActivity, cls);
        currentActivity.startActivity(nextActivity);
    }

    public void updateList(){
        if(currentFragment.isVisible()) {
            currentFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentFragment.updateList();
                }
            });
        }
    }

    public void enableLogin(){
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity activity = (MainActivity)currentActivity;
                activity.login.setEnabled(true);
            }
        });
    }
}
