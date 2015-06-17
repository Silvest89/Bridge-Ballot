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

public class BridgeFragment extends BallotList {
    public static ActivityHandler handler;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mBridges = Account.mBridgeList;
        ballot = this;
        getActivity().setTitle("Pick your bridge!");
    }

    @Override
    public void onDestroyView() {
        getActivity().setTitle(R.string.fragment_watch_list);
        super.onDestroyView();
    }

    @Override
    public void updateList() {
        mBridges = Account.mBridgeList;
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bridge, parent, false);

        handler = new ActivityHandler(this);

        Account.requestBridges();
        setupView(v);

        return v;
    }

}