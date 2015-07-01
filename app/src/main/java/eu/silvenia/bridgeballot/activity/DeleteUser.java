package eu.silvenia.bridgeballot.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_user_delete, parent, false);
        spinner = (Spinner) v.findViewById(R.id.spinner_users);
        handler = new ActivityHandler(this);
        button = (Button) v.findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userToDelete = spinner.getSelectedItem().toString();
                Account.deleteUser(userToDelete);
                Account.requestUsers();
            }
        });
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
}
