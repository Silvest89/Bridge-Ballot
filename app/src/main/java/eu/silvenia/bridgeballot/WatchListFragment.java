package eu.silvenia.bridgeballot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class WatchListFragment extends Fragment {
    ListView list;

    Integer[] imgid = {
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle
    };

    String[] itemname = {
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War",
            "test1",
            "test2"
    };

    int[] distance = {
            1,
            34,
            56,
            234,
            23,
            67,
            56,
            65,
            56,
            91
    };

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_watch_list, container, false);

        WatchListAdapter adapter = new WatchListAdapter(getActivity(),imgid, itemname, distance );
        list = (ListView) rootview.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Slecteditem = itemname[+position];
                Toast.makeText(getActivity(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

        return rootview;
    }
}
