package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import eu.silvenia.bridgeballot.network.NetworkService;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    public static Network network;
    public static String token;

    public static ActivityHandler handler;

    public Button login;
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    NetworkService mBoundService;
    boolean mIsBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        //EDITED PART
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mBoundService = ((NetworkService.LocalBinder)service).getService();
            //mBoundService.test();
            System.out.println(HelperTools.getCurrentTimeStamp() + "NetworkService bound.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
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

        login = (Button) findViewById(R.id.button);

        handler = new ActivityHandler(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope("profile"))
                .build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        network = new Network();
        Intent gcmIntentService = new Intent(this, MyIntentService.class);
        startService(gcmIntentService);
        doBindService();
    }

    @Override
    public void onClick(View v) {
        mGoogleApiClient.connect();
        //startActivity(new Intent(this, DetailPage.class));
    }

    public void onSignIn(View v){
        EditText userName = (EditText) findViewById(R.id.userName);
        EditText password = (EditText) findViewById(R.id.password);

        login.setEnabled(false);

        //Account.setUserName(userName.getText().toString());
        //Account.setPassword(password.getText().toString());
        Account.login(userName.getText().toString(), password.getText().toString(), false);
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
        boolean validateLogin = network.login(Plus.AccountApi.getAccountName(mGoogleApiClient), "", true, token);

        Toast.makeText(this, Plus.AccountApi.getAccountName(mGoogleApiClient), Toast.LENGTH_LONG).show();

        if(validateLogin)
            startActivity(new Intent(this, MenuActivity.class));
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

