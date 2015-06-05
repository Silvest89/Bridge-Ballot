package eu.silvenia.bridgeballot;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class WatchListFragment extends Fragment {
    ListView list;
    String[] itemname = {
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    Integer[] imgid = {
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
            R.mipmap.ic_redcircle,
    };
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_watch_list, container, false);

        WatchListAdapter adapter = new WatchListAdapter(getActivity(), itemname, imgid);
        list = (ListView) rootview.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = itemname[+position];
                Toast.makeText(getActivity(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });

        return rootview;
    }
}
