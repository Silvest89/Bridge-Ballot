package eu.silvenia.bridgeballot;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


public class WatchListFragment extends Fragment {
    View rootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_watch_list, container, false);

        LinearLayout topTitle = new LinearLayout(container.getContext());
        LinearLayout.LayoutParams topTitleParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        topTitle.setLayoutParams(topTitleParam);
        topTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView fragmentTitle = new TextView(container.getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,5,0,5);
        fragmentTitle.setLayoutParams(lp);
        fragmentTitle.setGravity(Gravity.CENTER);
        fragmentTitle.setText("WATCHLIST");
        fragmentTitle.setTextSize(30.0f);

        LinearLayout bridgeLayout = new LinearLayout(container.getContext());
        LinearLayout.LayoutParams bridgeParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250);
        bridgeParam.setMargins(0,30,0,30);
        bridgeLayout.setLayoutParams(bridgeParam);
        bridgeLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView bridgeTitle = new TextView(container.getContext());
        bridgeTitle.setLayoutParams(lp);
        bridgeTitle.setGravity(Gravity.CENTER);
        bridgeTitle.setTextSize(20.0f);
        bridgeTitle.setText("Spijkenisserbrug");





        bridgeLayout.addView(bridgeTitle);


        topTitle.addView(fragmentTitle);
        topTitle.addView(bridgeLayout);


        return topTitle;
    }
}
