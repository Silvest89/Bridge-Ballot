package eu.silvenia.bridgeballot;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bridgeballotserver.Bridge;


public class WatchListFragment extends Fragment {
    ListView list;

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_watch_list, container, false);

        HashMap<Integer, Bridge> bridgeMap = MainActivity.network.requestBridge();

        ArrayList<Bridge> bridges = new ArrayList<>(bridgeMap.values());

        BridgesAdapter adapter = new BridgesAdapter(getActivity(), bridges);
        list = (ListView) rootview.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bridge bridge = (Bridge)list.getItemAtPosition(position);
                Toast.makeText(getActivity(), bridge.getName(), Toast.LENGTH_SHORT).show();

            }
        });

        return rootview;
    }
}
