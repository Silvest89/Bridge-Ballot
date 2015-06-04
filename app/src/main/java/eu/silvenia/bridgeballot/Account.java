package eu.silvenia.bridgeballot;

/**
 * Created by Jesse on 29-5-2015.
 */
public final class Account {
    private static int id;
    private static String userName;
    private static boolean isGooglePlus;

    public static int getiD(){
        return id;
    }

    public static String getUserName(){
        return userName;
    }

    public static boolean getGooglePlus(){
        return isGooglePlus;
    }

    public static void setId(int userId){
        id = userId;
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static void setGooglePlus(boolean GooglePlus){
        isGooglePlus = GooglePlus;
    }

    public static void resetAccount(){
        setId(0);
        setUserName("");
        setGooglePlus(false);
    }


}
