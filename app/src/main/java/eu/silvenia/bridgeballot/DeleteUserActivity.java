package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jesse on 9-6-2015.
 */
public class DeleteUserActivity extends Activity {
    Network network;
    Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_delete);
        spinner = (Spinner) findViewById(R.id.spinner_users);

        try {
           populateSpinner();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void populateSpinner() throws ExecutionException, InterruptedException {
        network = MainActivity.network;
        ArrayList<String> users = network.requestUsers();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, users);
        spinner.setAdapter(adapter);
    }

    public void onDelete(View v) throws ExecutionException, InterruptedException {
        network = MainActivity.network;
        String userToDelete = spinner.getSelectedItem().toString();
        Integer result = network.deleteUser(userToDelete);

        final AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.cancel();
            }
        });
        if (result == 0){
            alert.setTitle("Success");
            alert.setMessage(getString(R.string.user_delete_success));
            alert.show();
        }

        else {
            alert.setTitle("Error");
            alert.setMessage(getString(R.string.user_delete_failure));
            alert.show();
        }
    }
}
