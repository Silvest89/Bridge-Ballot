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
    Bridge SelectedBridge;
    int id;
    TextView distance;
    TextView city;
    TextView status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_detail);

        id = getIntent().getExtras().getInt("ID");
        setBackgroundImage();
        SelectedBridge = Account.bridgeMap.get(id);
        distance = (TextView)findViewById(R.id.distance);
        city = (TextView)findViewById(R.id.city);
        status = (TextView)findViewById(R.id.currentStatus);
        distance.setText("Distance: " +Double.toString(SelectedBridge.getDistance()));
        city.setText("City: " + SelectedBridge.getLocation());
        getSupportActionBar().setTitle(SelectedBridge.getName());
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

    public void setBackgroundImage(){
        ImageView detailLayout = (ImageView) findViewById(R.id.imageViewBackground);
        switch(id){
            case 1: detailLayout.setBackgroundResource(R.drawable.bridge_1);
                    break;
            case 2: detailLayout.setBackgroundResource(R.drawable.bridge_2);
                    break;
            case 3: detailLayout.setBackgroundResource(R.drawable.bridge_3);
                break;
            case 4: detailLayout.setBackgroundResource(R.drawable.bridge_4);
                break;
            case 5: detailLayout.setBackgroundResource(R.drawable.bridge_5);
                break;
            case 6: detailLayout.setBackgroundResource(R.drawable.bridge_6);
                break;
            case 7: detailLayout.setBackgroundResource(R.drawable.bridge_7);
                break;
            case 8: detailLayout.setBackgroundResource(R.drawable.bridge_8);
                break;
            case 9: detailLayout.setBackgroundResource(R.drawable.bridge_9);
                break;
            case 10: detailLayout.setBackgroundResource(R.drawable.bridge_10);
                break;

        }
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
            SelectedBridge.setOpen(true);
            updateStatus();
    }

    public void updateStatus(){
        if (SelectedBridge.isOpen()) {
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
        SelectedBridge.setOpen(false);
        updateStatus();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(SelectedBridge.getLatitude(), SelectedBridge.getLongitude());

        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(SelectedBridge.getName()));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);

    }
}
