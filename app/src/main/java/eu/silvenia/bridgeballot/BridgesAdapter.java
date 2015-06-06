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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Bridge bridge = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.watchlist, parent, false);
        }
        // Lookup view for data population
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
        TextView extratxt = (TextView) convertView.findViewById(R.id.textView1);

        // Populate the data into the template view using the data object
        txtTitle.setText(bridge.getName());
        imageView.setImageResource(R.mipmap.ic_redcircle);
        extratxt.setText("Distance: " + " km");
        // Return the completed view to render on screen
        return convertView;
    }

}
