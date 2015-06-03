package eu.silvenia.bridgeballot;

/**
 * Created by Jesse on 29-5-2015.
 */
public final class Account {
    private static String ID;
    private static String userName;
    private static String password;

    public static String getID(){
        return ID;
    }

    public static String getUserName(){
        return userName;
    }

    public static String getPassword(){
        return password;
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static void setPassword(String passWord){
        password = passWord;
    }

    public static void resetAccount(){
        ID = "";
        userName = "";
        password = "";

    }


}
