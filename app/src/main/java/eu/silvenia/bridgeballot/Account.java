package eu.silvenia.bridgeballot;

/**
 * Created by Jesse on 29-5-2015.
 */
public class Account {
    private String ID;
    private String userName;
    private String password;

    public Account(){

    }


    public String getID(){
        return ID;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public void setUserName(String name){
        this.userName = name;
    }

    public void setPassword(String password){
        this.password = password;
    }


}