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

    public static HashMap<Integer, Bridge> bridgeMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_watch_list, container, false);

        if(bridgeMap.isEmpty())
            bridgeMap = MainActivity.network.requestWatchlist(Account.getiD());

        ArrayList<Bridge> bridges = new ArrayList<>(bridgeMap.values());
        BridgesAdapter adapter = new BridgesAdapter(getActivity(), bridges);
        list = (ListView) rootview.findViewById(R.id.watchList);
        list.setAdapter(adapter);

        return rootview;
    }
}
