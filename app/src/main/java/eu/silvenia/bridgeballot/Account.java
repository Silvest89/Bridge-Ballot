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
    private static String token;
    private static Channel channel;
    private static int reputation;

    public static HashMap<Integer, Bridge> bridgeMap = new HashMap<>();
    public static HashMap<Integer, Bridge> watchListMap = new HashMap<>();

    public static ArrayList<Bridge> mWatchList = new ArrayList<>();
    public static ArrayList<Bridge> mBridgeList = new ArrayList<>();

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

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Account.token = token;
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
        if(channel.isActive()){
            channel.close();
        }
        setChannel(null);
    }

    public static void createAccount(String username, String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();

            String[] accountDetails = {username, Base64.encodeToString(hash, Base64.DEFAULT)};
            ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.CREATE_ACCOUNT);
            message.add(accountDetails);

            Account.getChannel().writeAndFlush(message);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void login(String username, String password, boolean googlePlus){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();

            String[] login = {username, Base64.encodeToString(hash, Base64.DEFAULT)};
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

    public static void requestUsers(){
        System.out.println("Test1");
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.REQUEST_USERS);
        Account.getChannel().writeAndFlush(message);
        System.out.println("Test2");
    }

    public static void deleteUser(String userName){
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.DELETE_USER);
        message.add(userName);
        Account.getChannel().writeAndFlush(message);
    }

    public static void requestBridges(){
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.REQUEST_BRIDGE);
        Account.getChannel().writeAndFlush(message);
    }

    public static void updateBridgeStatus(int id, boolean isOpen){
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.BRIDGE_STATUS_UPDATE);
        message.add(id);
        message.add(isOpen);
        Account.getChannel().writeAndFlush(message);
        if(BridgeFragment.handler != null)
            BridgeFragment.handler.updateList();
        if(WatchListFragment.handler != null)
        WatchListFragment.handler.updateList();
        //BridgeFragment.mBridges.addAll(bridgeMap.values());
        //WatchListFragment.mBridges.addAll(watchListMap.values());
    }

    public static void getWatchList(){
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.REQUEST_WATCHLIST);
        Account.getChannel().writeAndFlush(message);
    }

    public static void addToWatchList(Bridge bridge){
        watchListMap.put(bridge.getId(), bridge);
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.WATCHLIST_ADD);
        message.add(bridge.getId());
        Account.getChannel().writeAndFlush(message);
        WatchListFragment.handler.updateList();
    }

    public static void removeFromWatchList(int id){
        watchListMap.remove(id);
        ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.WATCHLIST_DELETE);
        message.add(id);
        Account.getChannel().writeAndFlush(message);
    }

    public static void sendGcmToken(String token){
        if(Config.getGcmToken().equals("") || Config.getGcmToken().equals(null)) {
            ProtocolMessage message = new ProtocolMessage(NetworkHandler.MessageType.SEND_TOKEN);
            message.add(token);
            Account.getChannel().writeAndFlush(message);
            Config.setGcmToken(token);
        }
    }
}
