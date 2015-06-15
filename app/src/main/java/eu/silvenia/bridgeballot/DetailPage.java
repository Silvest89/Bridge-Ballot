package eu.silvenia.bridgeballot;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import eu.silvenia.bridgeballot.network.Bridge;

/**
 * Created by Jesse on 10-6-2015.
 */
public class DetailPage extends AppCompatActivity implements OnMapReadyCallback {
    Bridge selectedBridge;
    int id;
    TextView distance;
    TextView city;
    TextView status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_detail);
        ImageView detailLayout = (ImageView) findViewById(R.id.imageViewBackground);

        id = getIntent().getExtras().getInt("ID");

        selectedBridge = Account.bridgeMap.get(id);
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

        //LatLng latLng = new LatLng(SelectedBridge.getLatitude(), SelectedBridge.getLongitude());
        //CameraUpdate camera = CameraUpdateFactory.newLatLng(latLng);
        //mapView.getMap().animateCamera(camera);
        //mapView.getMap().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(SelectedBridge.getLatitude(), SelectedBridge.getLongitude())));

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

        //MainActivity.network.updateBridgeList(SelectedBridge.getId());
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
}
