package eu.silvenia.bridgeballot;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bridgeballotserver.Bridge;

public class BridgeListFragment extends Fragment implements Checkable {
    ListView list;
    View rootview;
    boolean bChecked = false;

    public static HashMap<Integer, Bridge> bridgeMap = new HashMap<>();

    public void updateBridgeDistance(){
        double longitude = GPSservice.longitude;
        double latitude = GPSservice.latitude;
        if(!bridgeMap.isEmpty()){
            Iterator it = bridgeMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Bridge bridge = (Bridge) pair.getValue();
                if(bridge != null) {
                    double distance = HelperTools.calculateGpsDistance(latitude, bridge.getLatitude(), longitude, bridge.getLongitude());
                    bridge.setDistance((int) distance);
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_bridge_list, container, false);

        if(bridgeMap.isEmpty())
            bridgeMap = MainActivity.network.requestBridge();

        updateBridgeDistance();
        ArrayList<Bridge> bridges = new ArrayList<>(bridgeMap.values());

        BridgesAdapter adapter = new BridgesAdapter(getActivity(), bridges);
        list = (ListView) rootview.findViewById(R.id.bridgeList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Bridge bridge = (Bridge)list.getItemAtPosition(position);
                //Toast.makeText(getActivity(), bridge.getName(), Toast.LENGTH_SHORT).show();
                toggle();

            }
        });

        return rootview;
    }

    @Override
    public void setChecked(boolean checked) {
        bChecked = checked;

    }

    @Override
    public boolean isChecked() {
        return bChecked;
    }

    @Override
    public void toggle() {
        bChecked = !bChecked;
    }
}