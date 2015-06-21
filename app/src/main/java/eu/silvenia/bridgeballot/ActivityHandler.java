package eu.silvenia.bridgeballot;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import eu.silvenia.bridgeballot.activity.BallotList;
import eu.silvenia.bridgeballot.activity.CreateUser;
import eu.silvenia.bridgeballot.activity.DeleteUser;
import eu.silvenia.bridgeballot.activity.DetailPage;
import eu.silvenia.bridgeballot.activity.Main;
import eu.silvenia.bridgeballot.activity.menufragment.BridgeList;

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

    public void updateList(){
        if(currentFragment.isVisible()) {
            currentFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BallotList ballotList = (BallotList) currentFragment;
                    if(ballotList instanceof BridgeList)
                        ballotList.updateList(Account.mBridgeList);
                    else
                        ballotList.updateList(Account.mWatchList);
                    }
            });
        }
    }

    public void enableLogin(){
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Main activity = (Main)currentActivity;
                activity.canLogin(true);
            }
        });
    }

    public void checkCreateAccount(final Integer result){
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CreateUser createUser = (CreateUser)currentActivity;
                if (result == 0)
                    HelperTools.showAlert(createUser, currentActivity.getString(R.string.alert_success), currentActivity.getString(R.string.alert_accountsucces));
                else if (result == 2){
                    HelperTools.showAlert(createUser, currentActivity.getString(R.string.alert_failure), currentActivity.getString(R.string.alert_accountfailure));
                }
            }

        });
    }

    public void checkUserDelete(final Integer result){
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result == 0){
                    HelperTools.showAlert(currentActivity, currentActivity.getString(R.string.alert_success), currentActivity.getString(R.string.alert_deletesuccess));
                }

                else {
                    HelperTools.showAlert(currentActivity, currentActivity.getString(R.string.alert_failure), currentActivity.getString(R.string.alert_deletefailure));
                }
            }
        });
    }

    public void getUsers(final ArrayList<String> users){
        currentFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (currentFragment instanceof DeleteUser) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(currentFragment.getActivity(), android.R.layout.simple_spinner_dropdown_item, users);
                        DeleteUser delete = (DeleteUser) currentFragment;
                        delete.populateSpinner(adapter);
                    }

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateRepList(){
        if(currentActivity instanceof DetailPage){
            final DetailPage detailPage = (DetailPage) currentActivity;
            detailPage.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    detailPage.mRecyclerView.getAdapter().notifyDataSetChanged();
                }
            });
        }
    }
}
