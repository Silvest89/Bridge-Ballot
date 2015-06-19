package eu.silvenia.bridgeballot;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.silvenia.bridgeballot.network.Bridge;

/**
 * Created by Johnnie Ho on 17-6-2015.
 */
public abstract class BallotList extends Fragment {

    protected BallotList ballot;
    private EditText searchbar;
    protected String title;
    private BridgeAdapter adapter;

    public RecyclerView mRecyclerView;
    protected MultiSelector mMultiSelector = new MultiSelector();

    public ArrayList<Bridge> mBridges = new ArrayList<>();

    public void setupView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.bridgelist_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new BridgeAdapter());

        HelperTools.updateBridgeDistance();
    }

    abstract public void updateList();

    @Override
    abstract public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        searchbar = (EditText) mRecyclerView.findViewById(R.id.search_bar);
        searchbar.addTextChangedListener(new TextWatcher() {
        //TODO
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
            Collections.sort(mBridges,new DistanceSorter());

            if(!bridge.isOpen()) {
                mStatusIcon.setImageResource(R.mipmap.ic_greencircle);
            }
            else{
                mStatusIcon.setImageResource(R.mipmap.ic_redcircle);
            }

            //mBridgeImage.setBackgroundResource(R.drawable.bridge_1);
            Bridge.setBackgroundImage(bridge, mBridgeImage, false);

            mTitleTextView.setText(bridge.getName());
            mDateTextView.setText("Distance: " + bridge.getDistance() + " m");
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

    protected class BridgeAdapter extends RecyclerView.Adapter<BridgeHolder> implements Filterable{
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

        @Override
        public Filter getFilter() {
            //TODO
            return null;
        }
    }
}
