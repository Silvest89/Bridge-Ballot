package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    public static Network network;
    public static String token;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope("profile"))
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        network = new Network();
        Intent test = new Intent(this, MyIntentService.class);
        startService(test);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClick(View v) {
        mGoogleApiClient.connect();
    }

    public void onSignIn(View v){
        EditText userName = (EditText) findViewById(R.id.userName);
        EditText password = (EditText) findViewById(R.id.password);
        System.out.println(token);
        //Account.setUserName(userName.getText().toString());
        //Account.setPassword(password.getText().toString());
        //boolean validateLogin = network.login(userName.getText().toString(), password.getText().toString(), false, token);

        //if(validateLogin)
            startActivity(new Intent(this, MenuActivity.class));
    }

    public void onCreateUser(View v){
        Intent createUser = new Intent(this, CreateUserActivity.class);
        startActivity(createUser);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        boolean mIntentInProgress = false;
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        Toast.makeText(this, Plus.AccountApi.getAccountName(mGoogleApiClient), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

}