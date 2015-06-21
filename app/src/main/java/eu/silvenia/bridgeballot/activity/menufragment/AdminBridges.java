package eu.silvenia.bridgeballot.activity.menufragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.Bridge;
import eu.silvenia.bridgeballot.HelperTools;
import eu.silvenia.bridgeballot.R;


public class AdminBridges extends Fragment {

    List<Bridge> bridgeList;

    EditText bridgeName;
    EditText bridgeLocation;
    EditText bridgeLatitude;
    EditText bridgeLongitude;

    Button saveButton;
    Button deleteButton;

    int bridgeID;

    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_admin_bridges, container, false);

        Spinner bridgeSpinner = (Spinner) rootview.findViewById(R.id.bridgeSpinner);
        bridgeName = (EditText) rootview.findViewById(R.id.bridgeName);
        bridgeLocation = (EditText) rootview.findViewById(R.id.bridgeLocation);
        bridgeLatitude = (EditText) rootview.findViewById(R.id.bridgeLatitude);
        bridgeLongitude = (EditText) rootview.findViewById(R.id.bridgeLongitude);
        saveButton = (Button) rootview.findViewById(R.id.saveButton);
        deleteButton = (Button) rootview.findViewById(R.id.deleteButton);


        bridgeList = Account.mBridgeList;

        final List<String> bridgeNames = new ArrayList<>();
        for(int b = 0; b < bridgeList.size(); b++){
            bridgeNames.add(bridgeList.get(b).getName());
        }
        bridgeNames.add(getString(R.string.bridgeadmin_newbridge));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, bridgeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bridgeSpinner.setAdapter(adapter);

        bridgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!(bridgeNames.get(position).equals(getString(R.string.bridgeadmin_newbridge)))) {
                    Bridge bridge = bridgeList.get(position);

                    bridgeName.setText(bridge.getName());
                    bridgeID = bridge.getId();
                    bridgeLocation.setText(bridge.getLocation());
                    bridgeLatitude.setText(String.valueOf(bridge.getLatitude()));
                    bridgeLongitude.setText(String.valueOf(bridge.getLongitude()));
                    saveButton.setText(getString(R.string.bridgeadmin_save));
                    deleteButton.setVisibility(View.VISIBLE);

                } else {
                    bridgeName.setText("");
                    bridgeLocation.setText("");
                    bridgeLatitude.setText("");
                    bridgeLongitude.setText("");
                    saveButton.setText(R.string.bridgeadmin_savenew);
                    deleteButton.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveButton.setEnabled(false);
                ArrayList<String> bridge = new ArrayList<>();
                bridge.add(String.valueOf(bridgeID));
                bridge.add(bridgeName.getText().toString());
                bridge.add(bridgeLocation.getText().toString());
                bridge.add(bridgeLatitude.getText().toString());
                bridge.add(bridgeLongitude.getText().toString());
                if(saveButton.getText().equals(getString(R.string.bridgeadmin_savenew))){
                    Account.CRUDBridge(CRUDType.CREATE, bridge );
                }else{
                    Account.CRUDBridge(CRUDType.UPDATE, bridge);
                }
                saveButton.setEnabled(true);
                HelperTools.showAlert(getActivity(), getString(R.string.alert_success), getString(R.string.alert_bridge_saved));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteButton.setEnabled(false);
                ArrayList<String> bridge = new ArrayList<String>();
                bridge.add(String.valueOf(bridgeID));
                Account.CRUDBridge(CRUDType.DELETE, bridge);
                deleteButton.setEnabled(true);
                Account.bridgeMap.remove(bridgeID);
                HelperTools.showAlert(getActivity(), getString(R.string.alert_success), getString(R.string.alert_bridge_deleted));
            }
        });
        return rootview;
    }

    public enum CRUDType {
        CREATE,
        UPDATE,
        DELETE
    }

}
