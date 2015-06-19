package eu.silvenia.bridgeballot.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.HelperTools;
import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.network.NetworkService;
import eu.silvenia.bridgeballot.services.GPSservice;
import eu.silvenia.bridgeballot.services.MyIntentService;

public class Main extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private Button login;
    private com.google.android.gms.common.SignInButton googleLogin;

    public void canLogin(boolean canLogin){
        login.setEnabled(canLogin);
        googleLogin.setEnabled(canLogin);
    }
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Reputation used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    NetworkService mBoundService;
    boolean mIsBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        //EDITED PART
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((NetworkService.LocalBinder)service).getService();
            mBoundService.connect();
            //mBoundService.test();
            System.out.println(HelperTools.getCurrentTimeStamp() + "NetworkService bound.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }

    };

    private void doBindService() {
        bindService(new Intent(this, NetworkService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }


    private void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, GPSservice.class));

        ActivityHandler.handler = new ActivityHandler(this);

        login = (Button) findViewById(R.id.button);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        googleLogin = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button);
        googleLogin.setOnClickListener(this);
        Intent gcmIntentService = new Intent(this, MyIntentService.class);
        startService(gcmIntentService);
        doBindService();
    }

    @Override
    public void onClick(View v) {
        mGoogleApiClient.connect();
    }

    public void onSignIn(View v){
        EditText userName = (EditText) findViewById(R.id.userName);
        EditText password = (EditText) findViewById(R.id.password);

        if(userName.length() == 0 || password.length() == 0) {
            HelperTools.showAlert(this, "Error", "Please fill out both fields.");
            return;
        }

        System.out.println(Account.getToken());
        login.setEnabled(false);
        googleLogin.setEnabled(false);

        Account.login(userName.getText().toString(), password.getText().toString(), false);
    }

    public void onCreateUser(View v){
        Intent createUser = new Intent(this, CreateUser.class);
        startActivity(createUser);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        boolean mIntentInProgress = false;
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                result.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
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
        //boolean validateLogin = network.login(Plus.AccountApi.getAccountName(mGoogleApiClient), "", true, token);
        login.setEnabled(false);
        googleLogin.setEnabled(false);
        Account.setGooglePlus(true);

        Account.login(Plus.AccountApi.getAccountName(mGoogleApiClient), "", true);
        //Toast.makeText(this, Plus.AccountApi.getAccountName(mGoogleApiClient), Toast.LENGTH_LONG).show();

        //if(validateLogin)
        //startActivity(new Intent(this, Menu.class));
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}