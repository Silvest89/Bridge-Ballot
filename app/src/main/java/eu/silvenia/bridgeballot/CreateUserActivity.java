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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);

    }

    public void makeAccount(View v) throws ExecutionException, InterruptedException {
        final EditText userNameET = (EditText) findViewById(R.id.create_userName);
        final EditText passwordET = (EditText) findViewById(R.id.create_password);
        final EditText confirmPasswordET = (EditText) findViewById(R.id.create_confirmPassword);

        String userName = userNameET.getText().toString();
        String password = passwordET.getText().toString();
        String confirmPassword = confirmPasswordET.getText().toString();

        if (userName.length() == 0 || password.length() == 0 || confirmPassword.length() == 0){
            final AlertDialog alert = new AlertDialog.Builder(this).create();
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
                Integer result = MainActivity.network.createAccount(userName, password);
                final AlertDialog alert = new AlertDialog.Builder(this).create();
                alert.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.cancel();
                    }
                });
                if (result == 0) {
                    alert.setTitle("Success");
                    alert.setMessage(getString(R.string.create_alertSuccess));
                    alert.show();
                    userNameET.setText(null);
                    passwordET.setText(null);
                    confirmPasswordET.setText(null);
                } else if (result == 2) {
                    alert.setTitle("Error");
                    alert.setMessage(getString(R.string.create_alertUserNotUnique));
                    alert.show();
                    userNameET.setText(null);
                    passwordET.setText(null);
                    confirmPasswordET.setText(null);
                }
            }
        }
    }
}
