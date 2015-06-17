package eu.silvenia.bridgeballot.network;

import java.util.Date;

/**
 * Created by Johnnie Ho on 17-6-2015.
 */
public class Client {
    private int id;
    private String userName;
    private int reputation;
    private Date timeStamp;
    private boolean status;

    public Client(int id, String userName, int reputation, Date timeStamp, boolean status){
        this.id = id;
        this.userName = userName;
        this.reputation = reputation;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public int getReputation() {
        return reputation;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public boolean getStatus() {
        return status;
    }
}