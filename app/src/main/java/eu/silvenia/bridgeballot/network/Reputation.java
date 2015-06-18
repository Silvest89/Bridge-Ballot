package eu.silvenia.bridgeballot.network;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Johnnie Ho on 17-6-2015.
 */
public class Reputation {
    private int voteId;
    private int accountId;
    private String userName;
    private int reputation;
    private Date timeStamp;
    private boolean status;
    private int bridgeId;

    public Reputation(int voteId, int accountId, String userName, int reputation, Date timeStamp, boolean status, int bridgeId){
        this.voteId = voteId;
        this.accountId = accountId;
        this.userName = userName;
        this.reputation = reputation;
        this.timeStamp = timeStamp;
        this.status = status;
        this.bridgeId = bridgeId;
    }

    public int getVoteId() {
        return voteId;
    }

    public void setVoteId(int voteId) {
        this.voteId = voteId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getBridgeId() {
        return bridgeId;
    }

    public void setBridgeId(int bridgeId) {
        this.bridgeId = bridgeId;
    }
}
