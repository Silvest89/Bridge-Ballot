package eu.silvenia.bridgeballot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import bridgeballotserver.Bridge;

/**
 * Created by Johnnie Ho on 6-6-2015.
 */
public class BridgesAdapter extends ArrayAdapter<Bridge> {
    public BridgesAdapter(Context context, ArrayList<Bridge> bridges) {
        super(context, R.layout.watchlist, bridges);
    }

    private static class ViewHolder {
        TextView bridgeName;
        ImageView status;
        ImageView addButton;
        TextView distance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Bridge bridge = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.watchlist, parent, false);
            viewHolder.bridgeName = (TextView) convertView.findViewById(R.id.item);
            viewHolder.status = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.addButton = (ImageView) convertView.findViewById(R.id.addButton);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.bridgeName.setText(bridge.getName());
        if(!bridge.isOpen()) {
            viewHolder.status.setImageResource(R.mipmap.ic_greencircle);
        }else{
            if(bridge.isOpen()){
                viewHolder.status.setImageResource(R.mipmap.ic_redcircle);
            }else{
                viewHolder.status.setImageResource(0);
            }
        }

        viewHolder.addButton.setImageResource(R.mipmap.ic_addbutton);
        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer result = MainActivity.network.sendBridgeToWatchlist(bridge.getId(), Account.getiD());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
        viewHolder.distance.setText("Distance: " + bridge.getDistance() +  " m");
        // Return the completed view to render on screen
        return convertView;
    }

}
