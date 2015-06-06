package eu.silvenia.bridgeballot;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BridgeListFragment extends Fragment {
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.activity_bridge_list, container, false);

        final ListView bridgeList = (ListView) rootview.findViewById(R.id.bridgeList);
        Network network = MainActivity.network;
        ArrayList<String> bridges = new ArrayList();
        /*ArrayList<String[]> bridgeArray = network.requestBridge();
        
        for(int x = 0; x < bridgeArray.size(); x++){
            String[] temp = bridgeArray.get(x);
            bridges.add(temp[1]);

        }*/
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