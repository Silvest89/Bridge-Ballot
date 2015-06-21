package eu.silvenia.bridgeballot.activity.menufragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.activity.BallotList;

public class BridgeList extends BallotList {
    public static ActivityHandler handler;

    private EditText searchbar;

    protected void setupSearchBar(View view){
        /*((BridgeAdapter) mRecyclerView.getAdapter()).flushFilter(true);
        searchbar = (EditText) view.findViewById(R.id.search_bar);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println(s.length());
                if(s.length() == 0)
                    ((BridgeAdapter) mRecyclerView.getAdapter()).flushFilter(false);
                else
                    ((BridgeAdapter) mRecyclerView.getAdapter()).setFilter(s.toString());
            }
        });*/
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mBridges = Account.mBridgeList;
        ballot = this;
        getActivity().setTitle(getString(R.string.title_activity_bridge_list));
    }

    @Override
    public void onDestroyView() {
        getActivity().setTitle(R.string.title_fragment_watch_list);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bridge, parent, false);

        handler = new ActivityHandler(this);

        if(Account.mBridgeList.isEmpty())
            Account.requestBridges();

        setupView(v);
        setupSearchBar(v);

        mRecyclerView.getAdapter().notifyDataSetChanged();
        return v;
    }

}