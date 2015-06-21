package eu.silvenia.bridgeballot.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.BallotSettings;
import eu.silvenia.bridgeballot.R;
import eu.silvenia.bridgeballot.activity.menufragment.About;
import eu.silvenia.bridgeballot.activity.menufragment.AdminBridges;
import eu.silvenia.bridgeballot.activity.menufragment.BridgeList;
import eu.silvenia.bridgeballot.activity.menufragment.WatchList;

public class Menu extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFragmentTitles;

    private FragmentLocation location;

    private enum FragmentLocation{
        BRIDGE_LIST,
        WATCH_LIST,
        ABOUT,
        ADMIN_BRIDGES,
        DELETE
    }

    /**
     * initialisation of variables and interface
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ActivityHandler.handler = new ActivityHandler(this);

        mTitle = mDrawerTitle = getTitle();
        mFragmentTitles = getResources().getStringArray(R.array.fragments_array);

        if (Account.getAccessLevel() != 0){
            mFragmentTitles = Arrays.copyOf(mFragmentTitles, mFragmentTitles.length - 2);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mFragmentTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        location = FragmentLocation.WATCH_LIST;
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    /**
     * Creates the option menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static boolean isVisible = true;

    /**
     * Called whenever we call invalidateOptionsMenu()
     * sets up the optionsmenu
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        switch(location){
            case WATCH_LIST:{
                if(!isVisible) {
                    menu.findItem(R.id.action_add).setVisible(false);
                    menu.findItem(R.id.action_remove).setVisible(true);
                }else{
                    menu.findItem(R.id.action_remove).setVisible(false);
                    menu.findItem(R.id.action_add).setVisible(true);
                }

                break;
            }
            default:{
                menu.findItem(R.id.action_add).setVisible(false);
                menu.findItem(R.id.action_remove).setVisible(false);
                break;
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * handles the top menu buttons
     * Starts the selected intent depending on the option selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        isVisible = true;
        Fragment fragment = null;
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_add:{
                isVisible = false;
                fragment = new BridgeList();
                break;
            }
            case R.id.action_remove:{
                fragment = new WatchList();
                break;
            }
            case R.id.action_settings:{
                startActivity(new Intent(this, BallotSettings.class));
                break;
            }
            case R.id.action_logout:{
                Account.resetAccount();
                startActivity(new Intent(this, Main.class));
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        invalidateOptionsMenu();
        if (fragment == null){
            return false;
        }
        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction().
                setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_down).
                addToBackStack(null).
                replace(R.id.content_frame, fragment).commit();
        return true;
    }


    /**
     * The click listener for ListView in the navigation drawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     *
     * starts the intent of selected screen in the navigation drawer
     * @param position
     */
    private void selectItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0:{
                fragment = new WatchList();
                location = FragmentLocation.WATCH_LIST;
                break;
            }
            case 1:{
                fragment = new About();
                location = FragmentLocation.ABOUT;
                break;
            }

            case 2 :{
                fragment = new DeleteUser();
                location = FragmentLocation.DELETE;
                break;
            }
            case 3: {
                fragment = new AdminBridges();
                location = FragmentLocation.ADMIN_BRIDGES;
                break;
            }
            default:
                break;
        }
        if(fragment == null)
            return;
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mFragmentTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /**
     * sets the title of an intent
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     *
     * Called after an intent has been created
     *
     *
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    /**
     * activates changes in the config
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * disable back button
     */
    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            return;
        } else {
            getFragmentManager().popBackStack();
        }

    }
    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
}