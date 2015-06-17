package eu.silvenia.bridgeballot;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import eu.silvenia.bridgeballot.network.Bridge;

public class WatchListFragment extends BallotList {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static ActivityHandler handler;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ballot = this;
        handler = new ActivityHandler(this);
        getActivity().setTitle("WatchList");
    }
    @Override
    public void onDestroyView() {
        getActivity().setTitle(R.string.fragment_watch_list);
        super.onDestroyView();
    }


    @Override
    public void updateList() {
        mBridges = Account.mWatchList;
        mRecyclerView.getAdapter().notifyDataSetChanged();
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
                Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG);
                Account.getWatchList();
                //refreshItems();
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
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.bridge_list_menu_context, menu);
    }
}