package eu.silvenia.bridgeballot.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.plus.PlusShare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import eu.silvenia.bridgeballot.*;
import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.Bridge;
import eu.silvenia.bridgeballot.Reputation;

/**
 * Created by Jesse on 10-6-2015.
 */
public class DetailPage extends AppCompatActivity implements OnMapReadyCallback {
    Bridge selectedBridge;

    TextView distance;
    TextView city;
    TextView status;

    Button shareButton;
    Button voteButton;

    public RecyclerView mRecyclerView;

    GoogleMap googleMap;

    public static ArrayList<Reputation> reputationList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHandler.handler = new ActivityHandler(this);

        int bridgeId = getIntent().getExtras().getInt("ID");

        setContentView(eu.silvenia.bridgeballot.R.layout.activity_bridge_detail);
        ImageView detailLayout = (ImageView) findViewById(R.id.imageViewBackground);
        shareButton = (Button) findViewById(R.id.button6);
        if(!Account.isGooglePlus())
            shareButton.setVisibility(View.GONE);

        selectedBridge = Account.bridgeMap.get(bridgeId);

        Account.sendReputationRequest(selectedBridge.getId());
        reputationList  = selectedBridge.repList;

        Bridge.setBackgroundImage(selectedBridge, detailLayout, true);

        distance = (TextView)findViewById(R.id.distance);
        city = (TextView)findViewById(R.id.city);
        status = (TextView)findViewById(R.id.currentStatus);
        distance.setText(Double.toString(selectedBridge.getDistance()) + " km");
        city.setText(selectedBridge.getLocation());
        voteButton = (Button)findViewById(R.id.button5);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(selectedBridge.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
        //if(canPress()) {
            selectedBridge.setOpen(true);
            Account.updateBridgeStatus(selectedBridge.getId(), true);
            updateStatus();
        //}
        //else
            //HelperTools.showAlert(this, getString(R.string.alert_error), getString(R.string.alert_distanceerror));
    }

    public void updateStatus(){
        if (selectedBridge.isOpen()) {
            status.setTextColor(Color.RED);
            status.setText(R.string.status_open);
            voteButton.setVisibility(View.INVISIBLE);
        }
        else {
            status.setTextColor(Color.GREEN);
            status.setText(R.string.status_closed);
            voteButton.setVisibility(View.VISIBLE);
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private boolean canPress(){
        if(selectedBridge.getDistance() < 0.5 && selectedBridge.getDistance() != 0) {
            return true;
        }

        return false;
    }

    public void googleShare(View v){
        googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                try {

                    Intent shareIntent = new PlusShare.Builder(ActivityHandler.handler.currentActivity)
                            .setType("video/*, image/*")
                            .setText("I am @ " + selectedBridge.getName())
                            .addStream(HelperTools.getImageUri(ActivityHandler.handler.currentActivity, bitmap))
                            //.setContentUrl(Uri.parse("https://developers.google.com/+/"))
                            .getIntent();

                    startActivityForResult(shareIntent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(selectedBridge.getLatitude(), selectedBridge.getLongitude());

        this.googleMap = googleMap;
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
        private Reputation mReputation;

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

        public void bindBridge(Reputation reputation) {
            mReputation = reputation;

            userName.setText(reputation.getUserName());
            button.setBackgroundResource(R.drawable.dislike_button);
            this.reputation.setText(Integer.toString(reputation.getReputation()));
            if(reputation.isStatus())
                status.setBackgroundResource(R.mipmap.ic_redcircle);
            else
                status.setBackgroundResource(R.mipmap.ic_greencircle);

            SimpleDateFormat sf = new SimpleDateFormat("dd-MM kk:mm:ss");
            String date = sf.format(reputation.getTimeStamp());
            timeStamp.setText(date);
        }

        @Override
        public void onClick(View v) {
            if(canPress()) {
                Account.sendReputationUpdate(mReputation.getVoteId(), Account.getId(), mReputation.getAccountId(), mReputation.getBridgeId());
            }
            else{
                HelperTools.showAlert(getApplicationContext(), getString(R.string.alert_error), getString(R.string.alert_distanceerror));
            }
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
            Reputation reputation = reputationList.get(pos);
            holder.bindBridge(reputation);
        }

        @Override
        public int getItemCount() {
            return reputationList.size();
        }
    }

}
