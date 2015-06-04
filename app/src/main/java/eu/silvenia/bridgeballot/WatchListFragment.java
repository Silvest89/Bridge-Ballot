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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class WatchListFragment extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_watch_list, container, false);

        RelativeLayout topTitle = new RelativeLayout(getActivity());

        RelativeLayout.LayoutParams topTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        topTitle.setPadding(0, 15, 0, 0);
        topTitle.setLayoutParams(topTitleParam);

        ViewGroup.LayoutParams fragmentTitleParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView fragmentTitle = new TextView(getActivity());
        fragmentTitle.setGravity(Gravity.CENTER);
        fragmentTitle.setText("Watchlist");
        fragmentTitle.setTextSize(40.0f);
        fragmentTitle.setId(10);
        topTitle.addView(fragmentTitle, fragmentTitleParam);

        RelativeLayout.LayoutParams bridgeLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 300);

        RelativeLayout bridgeLayout = new RelativeLayout(topTitle.getContext());
        bridgeLayout.setId(11);
        TextView fragmentTitle2 = new TextView(bridgeLayout.getContext());
        fragmentTitle2.setGravity(Gravity.CENTER);
        fragmentTitle2.setText("test");
        fragmentTitle2.setTextSize(25.0f);
        bridgeLayout.addView(fragmentTitle2, fragmentTitleParam);

        bridgeLayoutParam.addRule(RelativeLayout.BELOW, fragmentTitle.getId());
        bridgeLayoutParam.setMargins(0, 20, 0, 20);

        topTitle.addView(bridgeLayout, bridgeLayoutParam);

        return topTitle;
    }
}
