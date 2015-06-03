package eu.silvenia.bridgeballot;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BridgeList extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_list);
        ListView bridgeList = (ListView) findViewById(R.id.bridgeList);
        Network network = MainActivity.network;
        ArrayList<String> bridges = new ArrayList();
        try {
            ArrayList<String[]> bridgeArray = network.requestBridge();

            for(int x = 0; x < bridgeArray.size(); x++){
                String[] temp = bridgeArray.get(x);
                bridges.add(temp[1]);

            }
            ArrayAdapter<String> test = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bridges);
            bridgeList.setAdapter(test);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
