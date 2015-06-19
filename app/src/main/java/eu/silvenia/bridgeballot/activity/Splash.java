package eu.silvenia.bridgeballot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.MenuItem;

import eu.silvenia.bridgeballot.Config;
import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.network.NetworkService;

public class Splash extends Activity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Config.readConfig(this);
        startService(new Intent(Splash.this, NetworkService.class));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(Splash.this, Main.class);
                startActivity(mainActivity);
                finish();
            }
        }, 4000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
