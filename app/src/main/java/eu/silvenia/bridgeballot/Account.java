package eu.silvenia.bridgeballot;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import eu.silvenia.bridgeballot.network.Bridge;
import eu.silvenia.bridgeballot.network.NetworkHandler;
import eu.silvenia.bridgeballot.network.ProtocolMessage;
import io.netty.channel.Channel;

/**
 * Created by Jesse on 29-5-2015.
 */
public final class Account {
    private static int id = 0;
    private static String userName;
    private static int accessLevel;
    private static boolean googlePlus;
    private static Channel channel;

    public static HashMap<Integer, Bridge> bridgeMap = new HashMap<>();
    public static HashMap<Integer, Bridge> watchListMap = new HashMap<>();

    public static int getId(){
        return id;
    }

    public static void setId(int userId){
        id = userId;
    }

    public static String getUserName(){
        return userName;
    }

    public static void setUserName(String name) {
        userName = name;
    }

    public static int getAccessLevel(){
        return accessLevel;
    }

    public static void setAccessLevel(int level){
        accessLevel = level;
    }

    public static boolean isGooglePlus() {
        return googlePlus;
    }

    public static void setGooglePlus(boolean googlePlus) {
        Account.googlePlus = googlePlus;
    }

    public static Channel getChannel() {
        return channel;
    }

    public static void setChannel(Channel channel) {
        Account.channel = channel;
    }

    public static void resetAccount(){
        setId(0);
        setUserName("");
        setGooglePlus(false);
        setAccessLevel(0);
        setChannel(null);
    }

    public static void login(String username, String password, boolean googlePlus){
        ArrayList<Object> test = new ArrayList<>();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();

            String[] login = {username, Base64.encodeToString(hash, Base64.DEFAULT), ""};
            ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.LOGIN);
            message.add(login);
            message.add(googlePlus);

            Account.getChannel().writeAndFlush(message);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void requestBridges(){
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.REQUEST_BRIDGE);
        Account.getChannel().writeAndFlush(message);
    }

    public static synchronized void getWatchList(){
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.REQUEST_WATCHLIST);
        Account.getChannel().writeAndFlush(message);
    }

    public static synchronized void addToWatchList(Bridge bridge){
        watchListMap.put(bridge.getId(), bridge);
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.WATCHLIST_ADD);
        message.add(bridge.getId());
        Account.getChannel().writeAndFlush(message);
        WatchListFragment.handler.updateWatchList();
    }

    public static synchronized void removeFromWatchList(int id){
        watchListMap.remove(id);
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.WATCHLIST_DELETE);
        message.add(id);
        Account.getChannel().writeAndFlush(message);
    }
}
