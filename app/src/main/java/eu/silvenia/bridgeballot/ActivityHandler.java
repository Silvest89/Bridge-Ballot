package eu.silvenia.bridgeballot;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Johnnie Ho on 11-6-2015.
 */
public class ActivityHandler extends Handler {
    Activity currentActivity;
    BridgeFragment currentFragment;
    WatchListFragment watchListFragment;
    public ActivityHandler(Activity activity){
        this.currentActivity = activity;
    }

    public ActivityHandler(BridgeFragment fragment){
        this.currentFragment = fragment;
    }

    public ActivityHandler(WatchListFragment fragment) {this.watchListFragment = fragment;}

    public void switchActivity(Class<?> cls){
        Intent nextActivity = new Intent(currentActivity, cls);
        currentActivity.startActivity(nextActivity);
    }

    public void updateBridgeList(){
        if(currentFragment.isVisible()) {
            currentFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    currentFragment.mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
    }

    public void updateWatchList(){
        if(watchListFragment.isVisible()) {
            watchListFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //watchListFragment.mBridges = new ArrayList<>(Account.watchListMap.values());
                    watchListFragment.mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
    }
}
