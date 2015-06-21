package eu.silvenia.bridgeballot.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.R;

/**
 * Created by Jesse on 9-6-2015.
 */
public class DeleteUser extends Fragment {
    public static ActivityHandler handler;
    Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_user_delete, parent, false);
        spinner = (Spinner) v.findViewById(R.id.spinner_users);
        handler = new ActivityHandler(this);
        Account.requestUsers();

        return v;
    }

    /**
     * fills spinner with adapter data
     * @param adapter
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void populateSpinner(ArrayAdapter<String> adapter) throws ExecutionException, InterruptedException {
        spinner.setAdapter(adapter);
    }

    /**
     * delete selected user from spinner
     * @param v
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void onDelete(View v) throws ExecutionException, InterruptedException {
        String userToDelete = spinner.getSelectedItem().toString();
        Account.deleteUser(userToDelete);
    }
}
