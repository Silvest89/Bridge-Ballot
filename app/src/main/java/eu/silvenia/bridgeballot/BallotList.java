package eu.silvenia.bridgeballot;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
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

/**
 * Created by Johnnie Ho on 17-6-2015.
 */
public abstract class BallotList extends Fragment {

    protected BallotList ballot;

    protected String title;

    public RecyclerView mRecyclerView;
    protected MultiSelector mMultiSelector = new MultiSelector();

    public ArrayList<Bridge> mBridges = new ArrayList<>();

    protected void updateBridgeDistance(){
        double longitude = GPSservice.longitude;
        double latitude = GPSservice.latitude;
        if(!Account.bridgeMap.isEmpty()){
            Iterator it = Account.bridgeMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Bridge bridge = (Bridge) pair.getValue();
                if(bridge != null) {
                    double distance = HelperTools.calculateGpsDistance(latitude, bridge.getLatitude(), longitude, bridge.getLongitude());
                    bridge.setDistance((int) distance);
                }
            }
        }
    }

    public void setupView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.bridgelist_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new BridgeAdapter());

        updateBridgeDistance();
    }

    abstract public void updateList();

    @Override
    abstract public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    protected void selectBridge(Bridge c) {
        int index = mBridges.indexOf(c);
        int id = c.getId();
        BridgeHolder holder = (BridgeHolder) mRecyclerView
                .findViewHolderForPosition(index);
        Toast.makeText(getActivity(), c.getName(), Toast.LENGTH_SHORT).show();
        Intent DetailPage = new Intent(getActivity() , eu.silvenia.bridgeballot.DetailPage.class);
        DetailPage.putExtra("ID",id);
        startActivity(DetailPage);
    }

    ActionMode.Callback specialMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            if(ballot instanceof WatchListFragment)
                getActivity().getMenuInflater().inflate(R.menu.watch_list_menu_context, menu);
            else if(ballot instanceof BridgeFragment);
                getActivity().getMenuInflater().inflate(R.menu.bridge_list_menu_context, menu);

            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.bridge_menu_add: {
                    actionMode.finish();

                    mMultiSelector.setSelectable(false);

                    for (int i = mBridges.size() - 1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            Bridge bridge = mBridges.get(i);
                            //mRecyclerView.getAdapter().notifyItemRemoved(i);
                            Account.addToWatchList(bridge);
                        }
                    }

                    mMultiSelector.clearSelections();
                    return true;
                }
                case R.id.bridge_menu_delete: {
                    // Need to finish the action mode before doing the following,
                    // not after. No idea why, but it crashes.
                    actionMode.finish();

                    mMultiSelector.setSelectable(false);

                    for (int i = mBridges.size() - 1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            Bridge bridge = mBridges.get(i);
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

    private class BridgeHolder extends SwappingHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private final ImageView mBridgeImage;
        private final ImageView mStatusIcon;
        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final CheckBox mSolvedCheckBox;
        private Bridge mBridge;

        public BridgeHolder(View itemView) {
            super(itemView, mMultiSelector);

            mBridgeImage = (ImageView) itemView.findViewById(R.id.bridge_list_image);
            mStatusIcon =  (ImageView) itemView.findViewById(R.id.bridge_list_statusicon);
            mTitleTextView = (TextView) itemView.findViewById(R.id.bridge_list_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.bridge_list_distance);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.bridge_list_checkbox);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
        }

        public void bindBridge(Bridge bridge) {
            mBridge = bridge;


            if(!bridge.isOpen()) {
                mStatusIcon.setImageResource(R.mipmap.ic_greencircle);
            }
            else{
                mStatusIcon.setImageResource(R.mipmap.ic_redcircle);
            }

            //mBridgeImage.setBackgroundResource(R.drawable.bridge_1);
            Bridge.setBackgroundImage(bridge, mBridgeImage, false);

            mTitleTextView.setText(bridge.getName());
            mDateTextView.setText("Distance: " + bridge.getDistance() + " km");
            mSolvedCheckBox.setChecked(false);
        }

        @Override
        public void onClick(View v) {
            if (mBridge == null) {
                return;
            }
            if (!mMultiSelector.tapSelection(this)) {
                selectBridge(mBridge);
            }
        }

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

    protected class BridgeAdapter extends RecyclerView.Adapter<BridgeHolder> {
        @Override
        public BridgeHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bridge_list_layout, parent, false);
            return new BridgeHolder(view);
        }

        @Override
        public void onBindViewHolder(BridgeHolder holder, int pos) {
            Bridge bridge = mBridges.get(pos);
            holder.bindBridge(bridge);
        }

        @Override
        public int getItemCount() {
            return mBridges.size();
        }
    }
}
