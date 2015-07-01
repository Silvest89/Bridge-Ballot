package eu.silvenia.bridgeballot;


import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Johnnie Ho on 6-6-2015.
 */
public class Bridge implements Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    private int id;
    private String name;
    private String location;
    private double latitude, longitude;
    private double distance;
    private boolean isOpen;

    /**
     * defines what the values af the instance variables are for a bridge
     * @param id
     * @param name
     * @param location
     * @param latitude
     * @param longitude
     * @param isOpen
     */
    public Bridge(int id, String name, String location, double latitude, double longitude, boolean isOpen){
        this.id = id;
        this.name = name;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = 0;
        this.isOpen = isOpen;
    }

    public ArrayList<Reputation> repList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance(){
        return distance;
    }

    public void setDistance(double distance){
        this.distance = distance;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * sets the background based on the selected bridge
     * @param bridge
     * @param imageView
     * @param detailPage
     */
    public static void setBackgroundImage(Bridge bridge, ImageView imageView, boolean detailPage){
        if (detailPage == false) {
            switch (bridge.getId()) {
                case 1:
                    imageView.setBackgroundResource(R.drawable.bridge_list_1);
                    break;
                case 2:
                    imageView.setBackgroundResource(R.drawable.bridge_list_2);
                    break;
                case 3:
                    imageView.setBackgroundResource(R.drawable.bridge_list_3);
                    break;
                case 4:
                    imageView.setBackgroundResource(R.drawable.bridge_list_4);
                    break;
                case 5:
                    imageView.setBackgroundResource(R.drawable.bridge_list_5);
                    break;
                case 6:
                    imageView.setBackgroundResource(R.drawable.bridge_list_6);
                    break;
                case 7:
                    imageView.setBackgroundResource(R.drawable.bridge_list_7);
                    break;
                case 8:
                    imageView.setBackgroundResource(R.drawable.bridge_list_8);
                    break;
                case 9:
                    imageView.setBackgroundResource(R.drawable.bridge_list_9);
                    break;
                case 10:
                    imageView.setBackgroundResource(R.drawable.bridge_list_10);
                    break;

            }
        }else{
            switch (bridge.getId()) {
                case 1:
                    imageView.setBackgroundResource(R.drawable.bridge_1);
                    break;
                case 2:
                    imageView.setBackgroundResource(R.drawable.bridge_2);
                    break;
                case 3:
                    imageView.setBackgroundResource(R.drawable.bridge_3);
                    break;
                case 4:
                    imageView.setBackgroundResource(R.drawable.bridge_4);
                    break;
                case 5:
                    imageView.setBackgroundResource(R.drawable.bridge_5);
                    break;
                case 6:
                    imageView.setBackgroundResource(R.drawable.bridge_6);
                    break;
                case 7:
                    imageView.setBackgroundResource(R.drawable.bridge_7);
                    break;
                case 8:
                    imageView.setBackgroundResource(R.drawable.bridge_8);
                    break;
                case 9:
                    imageView.setBackgroundResource(R.drawable.bridge_9);
                    break;
                case 10:
                    imageView.setBackgroundResource(R.drawable.bridge_10);
                    break;
            }
        }
    }
}