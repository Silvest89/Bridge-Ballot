package eu.silvenia.bridgeballot;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eu.silvenia.bridgeballot.network.Bridge;
import eu.silvenia.bridgeballot.network.Client;

/**
 * Created by Jesse on 10-6-2015.
 */
public class DetailPage extends AppCompatActivity implements OnMapReadyCallback {
    Bridge selectedBridge;
    int id;
    TextView distance;
    TextView city;
    TextView status;

    RecyclerView mRecyclerView;

    public static ArrayList<Client> reputationList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHandler.handler = new ActivityHandler(this);

        reputationList.clear();

        setContentView(R.layout.activity_bridge_detail);
        ImageView detailLayout = (ImageView) findViewById(R.id.imageViewBackground);

        id = getIntent().getExtras().getInt("ID");

        selectedBridge = Account.bridgeMap.get(id);
        Account.sendReputationRequest(selectedBridge.getId());
        reputationList  = selectedBridge.repList;
        Bridge.setBackgroundImage(selectedBridge, detailLayout, true);

        distance = (TextView)findViewById(R.id.distance);
        city = (TextView)findViewById(R.id.city);
        status = (TextView)findViewById(R.id.currentStatus);
        distance.setText("Distance: " +Double.toString(selectedBridge.getDistance()));
        city.setText("City: " + selectedBridge.getLocation());

        getSupportActionBar().setTitle(selectedBridge.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.reputation_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ReputationAdapter());

        updateStatus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onVote(View v){
        selectedBridge.setOpen(true);
        Account.updateBridgeStatus(selectedBridge.getId(), true);
        updateStatus();
    }

    public void updateStatus(){
        if (selectedBridge.isOpen()) {
            status.setTextColor(Color.RED);
            status.setText("Open");
        }
        else {
            status.setTextColor(Color.GREEN);
            status.setText("Closed");
        }
    }

    public void resetStatus(View v){
        selectedBridge.setOpen(false);
        Account.updateBridgeStatus(selectedBridge.getId(), false);
        updateStatus();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(selectedBridge.getLatitude(), selectedBridge.getLongitude());

        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(selectedBridge.getName()));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
    }

    private class ReputationHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private final TextView userName;
        private final TextView reputation;
        private final TextView timeStamp;
        private final ImageView status;
        private final Button button;
        private Client mClient;

        public ReputationHolder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.rep_userName);
            reputation =  (TextView) itemView.findViewById(R.id.rep_reputation);
            timeStamp = (TextView) itemView.findViewById(R.id.rep_timeStamp);
            status = (ImageView) itemView.findViewById(R.id.rep_status);
            button = (Button) itemView.findViewById(R.id.rep_button);

            button.setOnClickListener(this);
            //itemView.setOnLongClickListener(this);
            //itemView.setLongClickable(true);
        }

        public void bindBridge(Client client) {
            mClient = client;

            userName.setText(client.getUserName());
            button.setBackgroundResource(R.drawable.dislike_button);
            reputation.setText(Integer.toString(client.getReputation()));
            if(client.getStatus())
                status.setBackgroundResource(R.mipmap.ic_redcircle);
            else
                status.setBackgroundResource(R.mipmap.ic_greencircle);

            SimpleDateFormat sf = new SimpleDateFormat("dd-MM kk:mm:ss");
            String date = sf.format(client.getTimeStamp());
            timeStamp.setText(date);
        }

        @Override
        public void onClick(View v) {
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }

    private class ReputationAdapter extends RecyclerView.Adapter<ReputationHolder> {
        @Override
        public ReputationHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.reputation_list, parent, false);
            return new ReputationHolder(view);
        }

        @Override
        public void onBindViewHolder(ReputationHolder holder, int pos) {
            Client client = reputationList.get(pos);
            holder.bindBridge(client);
        }

        @Override
        public int getItemCount() {
            return reputationList.size();
        }
    }
}
