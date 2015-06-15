package eu.silvenia.bridgeballot;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eu.silvenia.bridgeballot.network.Bridge;

public class BridgeFragment extends Fragment {

    public static ActivityHandler handler;

    public RecyclerView mRecyclerView;

    private MultiSelector mMultiSelector = new MultiSelector();

    public static ArrayList<Bridge> mBridges = new ArrayList<>();

    public void updateBridgeDistance(){
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.fragment_add_bridge);
        setRetainInstance(true);
    }

    @Override
    public void onDestroyView() {
        getActivity().setTitle(R.string.fragment_watch_list);
        super.onDestroyView();
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bridge, parent, false);

        handler = new ActivityHandler(this);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.bridge_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new BridgeAdapter());

        updateBridgeDistance();

        return v;
    }

    private void selectBridge(Bridge c) {
        int index = mBridges.indexOf(c);
        int id = c.getId();
        BridgeHolder holder = (BridgeHolder) mRecyclerView
                .findViewHolderForPosition(index);
        Toast.makeText(getActivity(), c.getName(), Toast.LENGTH_SHORT).show();
        Intent DetailPage = new Intent(getActivity() , eu.silvenia.bridgeballot.DetailPage.class);
        DetailPage.putExtra("ID",id);
        startActivity(DetailPage);

        /*FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().
                setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up).
                addToBackStack(null).
                replace(R.id.content_frame, new WatchListFragment()).commit();
        MenuActivity.isVisible = true;
        getActivity().invalidateOptionsMenu();*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_bridge_list, menu);
    }

    ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.bridge_list_menu_context, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.bridge_menu_add:
                    actionMode.finish();

                    mMultiSelector.setSelectable(false);

                    for (int i = mBridges.size()-1; i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            Bridge bridge = mBridges.get(i);
                            //mRecyclerView.getAdapter().notifyItemRemoved(i);
                            Account.addToWatchList(bridge);
                        }
                    }

                    mMultiSelector.clearSelections();
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

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
            mStatusIcon = (ImageView) itemView.findViewById(R.id.bridge_list_statusicon);
            mTitleTextView = (TextView) itemView.findViewById(R.id.bridge_list_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.bridge_list_distance);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.bridge_list_checkbox);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setLongClickable(true);
        }

        public void bindBridge(Bridge bridge) {
            mBridge = bridge;

            if (!bridge.isOpen()) {
                mStatusIcon.setImageResource(R.mipmap.ic_greencircle);
            } else {
                mStatusIcon.setImageResource(R.mipmap.ic_redcircle);
            }

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
            //mMultiSelector.clearSelections();
            //mMultiSelector.setSelectable(!mMultiSelector.isSelectable());
            //System.out.println(mMultiSelector.getSelectedPositions().size());
            ActionBarActivity activity = (ActionBarActivity) getActivity();
            activity.startSupportActionMode(mDeleteMode);
            mMultiSelector.setSelectable(!mMultiSelector.isSelectable());
            mMultiSelector.setSelected(this, true);
            return true;
        }
    }

    private class BridgeAdapter extends RecyclerView.Adapter<BridgeHolder> {
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