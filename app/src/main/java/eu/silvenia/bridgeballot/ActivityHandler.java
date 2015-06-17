package eu.silvenia.bridgeballot;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Johnnie Ho on 11-6-2015.
 */
public class ActivityHandler extends Handler {
    public static ActivityHandler handler;
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

    public void checkCreateAccount(final Integer result){
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result == 0)
                    HelperTools.showAlert(currentActivity, "Success", currentActivity.getString(R.string.create_alertSuccess));
                else if (result == 2){
                    HelperTools.showAlert(currentActivity, "Failure", currentActivity.getString(R.string.create_alertFailure));
                }
            }
        });
    }

    public void checkUserDelete(final Integer result){
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result == 0){
                    HelperTools.showAlert(currentActivity, "Success", currentActivity.getString(R.string.user_delete_success));
                }

                else {
                    HelperTools.showAlert(currentActivity, "Failure", currentActivity.getString(R.string.user_delete_failure));
                }
            }
        });
    }

    public void getUsers(final ArrayList<String> users){
        System.out.println("Test6");
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(currentActivity, android.R.layout.simple_spinner_dropdown_item, users);
                    DeleteUserActivity delete = (DeleteUserActivity) currentActivity;
                    System.out.println("Test7");
                    delete.populateSpinner(adapter);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
