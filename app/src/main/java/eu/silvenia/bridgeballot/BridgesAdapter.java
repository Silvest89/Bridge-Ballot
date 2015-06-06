package eu.silvenia.bridgeballot;

import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
        TextView distance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Bridge bridge = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.watchlist, parent, false);
            viewHolder.bridgeName = (TextView) convertView.findViewById(R.id.item);
            viewHolder.status = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.textView1);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.bridgeName.setText(bridge.getName());
        viewHolder.status.setImageResource(R.mipmap.ic_redcircle);
        viewHolder.distance.setText("Distance: " + " km");
        // Return the completed view to render on screen
        return convertView;
    }

}
