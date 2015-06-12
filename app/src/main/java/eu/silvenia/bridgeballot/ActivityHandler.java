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
    public Fragment currentFragment;
    public ActivityHandler(Activity activity){
        this.currentActivity = activity;
    }

    public ActivityHandler(Fragment fragment){
        this.currentFragment = fragment;
    }

    public void switchActivity(Class<?> cls){
        Intent nextActivity = new Intent(currentActivity, cls);
        currentActivity.startActivity(nextActivity);
    }

    public void updateBridgeList(){
        if(currentFragment.isVisible()) {
            currentFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BridgeFragment fragment = (BridgeFragment)currentFragment;
                    fragment.mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
    }

    public void updateWatchList(){

        if(currentFragment.isVisible()) {
            currentFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //watchListFragment.mBridges = new ArrayList<>(Account.watchListMap.values());
                    WatchListFragment fragment = (WatchListFragment)currentFragment;
                    fragment.mRecyclerView.getAdapter().notifyDataSetChanged();
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
