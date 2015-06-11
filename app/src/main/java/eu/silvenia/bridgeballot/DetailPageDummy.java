package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import eu.silvenia.bridgeballot.network.Bridge;

/**
 * Created by Jesse on 10-6-2015.
 */
public class DetailPageDummy extends Activity {
    Bridge spijkeniserBrug;
    TextView statusTV;
    int id = getIntent().getExtras().getInt("ID");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_detail);
        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        statusTV = (TextView) findViewById(R.id.currentStatus);

        spijkeniserBrug = Account.bridgeMap.get(1);
        updateStatus();
    }

    public void onVote(View v){
            spijkeniserBrug.setOpen(true);
            updateStatus();
    }

    public void updateStatus(){
        if (spijkeniserBrug.isOpen()) {
            statusTV.setText("Open");
        }
        else {
            statusTV.setText("Closed");
        }

        MainActivity.network.updateBridgeList(spijkeniserBrug.getId());
    }

    public void resetStatus(View v){
        spijkeniserBrug.setOpen(false);
        updateStatus();
    }


}
