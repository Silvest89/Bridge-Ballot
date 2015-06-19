package eu.silvenia.bridgeballot.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.R;

/**
 * Created by Jesse on 9-6-2015.
 */
public class DeleteUser extends Activity {
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_delete);
        spinner = (Spinner) findViewById(R.id.spinner_users);
        ActivityHandler.handler = new ActivityHandler(this);
        Account.requestUsers();
    }

    public void populateSpinner(ArrayAdapter<String> adapter) throws ExecutionException, InterruptedException {
        System.out.println("Test8");
        spinner.setAdapter(adapter);
    }

    public void onDelete(View v) throws ExecutionException, InterruptedException {
        String userToDelete = spinner.getSelectedItem().toString();
        Account.deleteUser(userToDelete);
    }
}
