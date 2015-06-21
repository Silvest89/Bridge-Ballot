package eu.silvenia.bridgeballot.activity.menufragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.Bridge;
import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.activity.BallotList;

public class WatchList extends BallotList {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static ActivityHandler handler;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ballot = this;
        getActivity().setTitle(getString(R.string.title_fragment_watch_list));
    }

    @Override
    public void onDestroyView() {
        getActivity().setTitle(R.string.title_fragment_watch_list);
        super.onDestroyView();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_watch, parent, false);

        mBridges = Account.mWatchList;
        handler = new ActivityHandler(this);

        setupView(v);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Account.getWatchList();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        // init swipe to dismiss logic
        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // callback for swipe to dismiss, removing item from data and adapter
                switch(direction){
                    case ItemTouchHelper.LEFT:{
                        Bridge bridge = mBridges.get(viewHolder.getAdapterPosition());
                        Account.watchListMap.remove(bridge.getId());
                        Account.removeFromWatchList(bridge.getId());
                        mBridges.remove(viewHolder.getAdapterPosition());

                        mRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                        break;
                    }
                    default:{
                        return;
                    }
                }
            }
        });
        swipeToDismissTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}