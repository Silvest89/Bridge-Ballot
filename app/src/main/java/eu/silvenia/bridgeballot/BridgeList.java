package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BridgeList extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_list);
        final ListView bridgeList = (ListView) findViewById(R.id.bridgeList);
        Network network = MainActivity.network;
        ArrayList<String> bridges = new ArrayList();
        /*ArrayList<String[]> bridgeArray = network.requestBridge();

        for(int x = 0; x < bridgeArray.size(); x++){
            String[] temp = bridgeArray.get(x);
            bridges.add(temp[1]);

        }*/
        ArrayAdapter<String> test = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bridges);
        bridgeList.setAdapter(test);

        bridgeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object selectedBrige = bridgeList.getItemAtPosition(position);
                //return selectedBridge to watchlist


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bridge_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
