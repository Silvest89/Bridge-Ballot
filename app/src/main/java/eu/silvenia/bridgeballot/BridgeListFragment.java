package eu.silvenia.bridgeballot;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bridgeballotserver.Bridge;

public class BridgeListFragment extends Fragment {
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_bridge_list, container, false);

        final ListView bridgeList = (ListView) rootview.findViewById(R.id.bridgeList);
        Network network = MainActivity.network;
        ArrayList<String> bridges = new ArrayList();
        HashMap<Integer, Bridge> bridgeMap = network.requestBridge();

        Iterator it = bridgeMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Bridge bridge = (Bridge) pair.getValue();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            bridges.add(bridge.getName());
        }

        ArrayAdapter<String> test = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, bridges);
        bridgeList.setAdapter(test);

        bridgeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object selectedBrige = bridgeList.getItemAtPosition(position);
                //return selectedBridge to watchlist


            }
        });

        return rootview;
    }
}