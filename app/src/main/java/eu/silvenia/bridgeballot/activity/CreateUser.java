package eu.silvenia.bridgeballot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.R;

/**
 * Created by Jesse on 3-6-2015.
 */
public class CreateUser extends Activity {

    AlertDialog alert;

    EditText userNameET;
    EditText passwordET;
    EditText confirmPasswordET;

    String userName;
    String password;
    String confirmPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);
        ActivityHandler.handler = new ActivityHandler(this);
        alert = new AlertDialog.Builder(this).create();
    }

    /**
     * Function that handles creating an account
     * @param v
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void makeAccount(View v) throws ExecutionException, InterruptedException {
        userNameET = (EditText) findViewById(R.id.create_userName);
        passwordET = (EditText) findViewById(R.id.create_password);
        confirmPasswordET = (EditText) findViewById(R.id.create_confirmPassword);

        userName = userNameET.getText().toString();
        password = passwordET.getText().toString();
        confirmPassword = confirmPasswordET.getText().toString();

        if (userName.length() == 0 || password.length() == 0 || confirmPassword.length() == 0){

            alert.setTitle(getString(R.string.alert_error));
            alert.setMessage(getString(R.string.alert_emptyfields));
            alert.setButton(getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.cancel();
                }
            });
            alert.show();
        }

        else {
            if (!password.equals(confirmPassword)) {
                final AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setTitle(getString(R.string.alert_error));
                alert.setMessage(getString(R.string.alert_message));
                alert.setButton(getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.cancel();
                    }
                });
                alert.show();
                passwordET.setText(null);
                confirmPasswordET.setText(null);

            } else {
                Account.createAccount(userName, password);
            }
        }
    }
}
