package eu.silvenia.bridgeballot.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.StateSet;
import android.view.*;
import android.view.Menu;
import android.widget.*;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.HelperTools;
import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.activity.menufragment.BridgeList;
import eu.silvenia.bridgeballot.activity.menufragment.WatchList;

/**
 * Created by Johnnie Ho on 17-6-2015.
 */
public abstract class BallotList extends Fragment {

    protected BallotList ballot;

    protected String title;

    protected RecyclerView mRecyclerView;
    protected MultiSelector mMultiSelector = new MultiSelector();

    protected ArrayList<eu.silvenia.bridgeballot.Bridge> mBridges = new ArrayList<>();

    public void setupView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.bridgelist_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new BridgeAdapter());

        HelperTools.updateBridgeDistance();

    }

    /**
    *Updates the list
    *@param list
    */
    public void updateList(ArrayList list){
        mBridges = list;
        ((BridgeAdapter) mRecyclerView.getAdapter()).flushFilter(true);
        sortList();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    /**
     * Sorts the list by distance
     */
    protected void sortList(){
        Collections.sort(mBridges,new DistanceSorter());
    }

    @Override
    abstract public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    /**
     * Triggers when bridge is selected
     * Starts the detailpage intent for the selectedbridge
     * @param c
     */
    protected void selectBridge(eu.silvenia.bridgeballot.Bridge c) {
        int index = mBridges.indexOf(c);
        int id = c.getId();
        BridgeHolder holder = (BridgeHolder) mRecyclerView
                .findViewHolderForPosition(index);

        Toast.makeText(getActivity(), c.getName(), Toast.LENGTH_SHORT).show();
        Intent DetailPage = new Intent(getActivity() , eu.silvenia.bridgeballot.activity.DetailPage.class);
        DetailPage.putExtra("ID",id);
        startActivity(DetailPage);
    }


    ActionMode.Callback specialMode = new ModalMultiSelectorCallback(mMultiSelector) {

        /**
         * Creates sidemenu depending on the current intent
         * @param actionMode
         * @param menu
         * @return
         */
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            if(ballot instanceof WatchList)
                getActivity().getMenuInflater().inflate(R.menu.watch_list_menu_context, menu);
            else if(ballot instanceof BridgeList)
                getActivity().getMenuInflater().inflate(R.menu.bridge_list_menu_context, menu);

            return true;
        }

        /**
         * Deletes or add bridge, depending on the option selected
         * @param actionMode
         * @param menuItem
         * @return
         */
        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.bridge_menu_add: {
                    actionMode.finish();

                    mMultiSelector.setSelectable(false);

                    for (int i = mBridges.size() - 1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            eu.silvenia.bridgeballot.Bridge bridge = mBridges.get(i);
                            Account.addToWatchList(bridge);
                        }
                    }

                    mMultiSelector.clearSelections();
                    return true;
                }
                case R.id.bridge_menu_delete: {
                    actionMode.finish();

                    mMultiSelector.setSelectable(false);

                    for (int i = mBridges.size() - 1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            eu.silvenia.bridgeballot.Bridge bridge = mBridges.get(i);
                            Account.removeFromWatchList(bridge.getId());
                            mBridges.remove(i);
                            mRecyclerView.getAdapter().notifyItemRemoved(i);
                        }
                    }

                    mMultiSelector.clearSelections();
                    return true;
                }
                default:
                    break;
            }
            return false;
        }
    };

    /**
     * Class with functionality for sorting list by distance
     */
    public class DistanceSorter implements Comparator<eu.silvenia.bridgeballot.Bridge> {
        @Override
        public int compare(eu.silvenia.bridgeballot.Bridge c1, eu.silvenia.bridgeballot.Bridge c2) {
            return Double.compare(c1.getDistance(), c2.getDistance());
        }
    }

    /**
     * This class holds all the interface variables
     */
    private class BridgeHolder extends SwappingHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ImageView mBridgeImage;
        private final ImageView mStatusIcon;
        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private eu.silvenia.bridgeballot.Bridge mBridge;

        public BridgeHolder(View itemView) {
            super(itemView, mMultiSelector);

            mBridgeImage = (ImageView) itemView.findViewById(R.id.bridge_list_image);
            mStatusIcon =  (ImageView) itemView.findViewById(R.id.bridge_list_statusicon);
            mTitleTextView = (TextView) itemView.findViewById(R.id.bridge_list_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.bridge_list_distance);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);

            Drawable colorDrawable = new ColorDrawable(Color.rgb(244, 164, 96));

            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_activated}, colorDrawable);
            stateListDrawable.addState(StateSet.WILD_CARD, null);

            setSelectionModeBackgroundDrawable(stateListDrawable);
        }

        /**
         * fills row of a bridge in the list with data
         * @param bridge
         */
        public void bindBridge(eu.silvenia.bridgeballot.Bridge bridge) {
            mBridge = bridge;

            if(!bridge.isOpen()) {
                mStatusIcon.setImageResource(R.mipmap.ic_greencircle);
            }
            else{
                mStatusIcon.setImageResource(R.mipmap.ic_redcircle);
            }

            eu.silvenia.bridgeballot.Bridge.setBackgroundImage(bridge, mBridgeImage, false);

            mTitleTextView.setText(bridge.getName());
            mDateTextView.setText(getString(R.string.ballotlist_distance) + bridge.getDistance() + " km");
        }

        /**
         * trigger function to goto detailpage of bridge when tapped upon (and is not done after a long click)
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mBridge == null) {
                return;
            }
            if (!mMultiSelector.tapSelection(this)) {
                selectBridge(mBridge);
            }
        }

        /**
         * on longclick select bridges
         * @param v
         * @return
         */
        @Override
        public boolean onLongClick(View v) {
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            activity.startSupportActionMode(specialMode);
            //activity.startActionMode(specialMode);
            mMultiSelector.setSelectable(!mMultiSelector.isSelectable());
            mMultiSelector.setSelected(this, true);
            return true;
        }
    }

    protected class BridgeAdapter extends RecyclerView.Adapter<BridgeHolder> implements Filterable{
        private List<eu.silvenia.bridgeballot.Bridge> allObjects = new ArrayList<>();

        @Override
        public BridgeHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bridge_list_layout, parent, false);

            return new BridgeHolder(view);
        }

        @Override
        public void onBindViewHolder(BridgeHolder holder, int pos) {
            eu.silvenia.bridgeballot.Bridge bridge = mBridges.get(pos);
            holder.bindBridge(bridge);
        }

        public void flushFilter(boolean newData){
            if(newData)
                allObjects.addAll(mBridges);
            else {
                mBridges = new ArrayList<>();
                mBridges.addAll(allObjects);
            }
            notifyDataSetChanged();
        }

        public void setFilter(String queryText) {
            mBridges = new ArrayList<>();
            queryText = queryText.toString().toLowerCase();
            for (eu.silvenia.bridgeballot.Bridge bridge: allObjects) {
                if (bridge.getName().toLowerCase().contains(queryText) || bridge.getLocation().toLowerCase().contains(queryText))
                    mBridges.add(bridge);
            }
            notifyDataSetChanged();
        }

        /**
         * returns item count of mBridges
         * @return
         */
        @Override
        public int getItemCount() {
            return mBridges.size();
        }

        @Override
        public Filter getFilter() {
            //TODO
            return null;
        }
    }
}
