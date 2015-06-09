package bridgeballotserver;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Johnnie Ho on 6-6-2015.
 */
public class Bridge implements Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    private int id;
    private String name;
    private String location;
    private double latitude, longitude;
    private int distance;
    private boolean status;

    public Bridge(int id, String name, String location, double latitude, double longitude, boolean status){
        this.id = id;
        this.name = name;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = 0;
        this.status = status;
    }

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

    public int getDistance(){
        return distance;
    }

    public void setDistance(int distance){
        this.distance = distance;
    }

    public boolean getStatus(){
        return status;
    }
}