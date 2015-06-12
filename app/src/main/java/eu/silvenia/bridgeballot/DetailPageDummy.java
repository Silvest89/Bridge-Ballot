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
    Bridge SelectedBridge;
    int id;
    TextView distance;
    TextView name;
    TextView city;
    TextView status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_detail);
        id = getIntent().getExtras().getInt("ID");
        SelectedBridge = Account.bridgeMap.get(id);
         distance = (TextView)findViewById(R.id.distance);
         name = (TextView)findViewById(R.id.name);
         city = (TextView)findViewById(R.id.city);
         status = (TextView)findViewById(R.id.status);
        name.setText(SelectedBridge.getName());
        distance.setText("Distance: " +Double.toString(SelectedBridge.getDistance()));
        city.setText("City: " + SelectedBridge.getLocation());

        updateStatus();
    }

    public void onVote(View v){
            SelectedBridge.setOpen(true);
            updateStatus();
    }

    public void updateStatus(){
        if (SelectedBridge.isOpen()) {
            status.setText("Open");
        }
        else {
            status.setText("Closed");
        }

        MainActivity.network.updateBridgeList(SelectedBridge.getId());
    }

    public void resetStatus(View v){
        SelectedBridge.setOpen(false);
        updateStatus();
    }


}
