package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

/**
 * Created by Jesse on 3-6-2015.
 */
public class CreateUserActivity extends Activity {

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

    public void makeAccount(View v) throws ExecutionException, InterruptedException {
        userNameET = (EditText) findViewById(R.id.create_userName);
        passwordET = (EditText) findViewById(R.id.create_password);
        confirmPasswordET = (EditText) findViewById(R.id.create_confirmPassword);

        userName = userNameET.getText().toString();
        password = passwordET.getText().toString();
        confirmPassword = confirmPasswordET.getText().toString();

        if (userName.length() == 0 || password.length() == 0 || confirmPassword.length() == 0){

            alert.setTitle("Error");
            alert.setMessage(getString(R.string.create_emptyFields));
            alert.setButton("OK", new DialogInterface.OnClickListener() {
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
                alert.setTitle("Error");
                alert.setMessage(getString(R.string.create_alertMessage));
                alert.setButton("OK", new DialogInterface.OnClickListener() {
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
