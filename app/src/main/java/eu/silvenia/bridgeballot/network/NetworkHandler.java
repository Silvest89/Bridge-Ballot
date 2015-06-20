package eu.silvenia.bridgeballot.network;

/**
 * Created by Johnnie Ho on 10-6-2015.
 */
import java.util.ArrayList;
import java.util.Date;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.ActivityHandler;
import eu.silvenia.bridgeballot.Bridge;
import eu.silvenia.bridgeballot.HelperTools;
import eu.silvenia.bridgeballot.Reputation;
import eu.silvenia.bridgeballot.activity.Menu;
import eu.silvenia.bridgeballot.activity.menufragment.WatchList;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Handler implementation for the object echo client.  It initiates the
 * ping-pong traffic between the object echo client and server by sending the
 * first message to the server.
 */
public class NetworkHandler extends ChannelHandlerAdapter {

    /**
     * Class which contains all constants which are needed for client-server communication
     */
    public final static class MessageType {
        public static final int LOGIN = 0;
        public static final int DISCONNECT = 1;
        public static final int SEND_TOKEN = 2;

        public static final int CREATE_ACCOUNT = 5;
        public static final int REQUEST_USERS = 6;
        public static final int DELETE_USER = 7;

        public static final int REQUEST_BRIDGE = 10;
        public static final int REQUEST_WATCHLIST = 11;
        public static final int WATCHLIST_ADD = 12;
        public static final int WATCHLIST_DELETE = 13;

        public static final int BRIDGE_STATUS_UPDATE = 14;
        public static final int BRIDGE_CREATE = 15;
        public static final int BRIDGE_UPDATE = 16;
        public static final int BRIDGE_DELETE = 17;

        public static final int REPUTATION = 18;
        public static final int REPUTATION_CHANGE = 19;
    }

    /**
     * Creates a client-side handler.
     */
    public NetworkHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //System.out.println(ctx.channel().id());
    }

    /**
     * Method which reads messages from the server and calls the right methods to read server input.
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Echo back the received object to the server.

        ProtocolMessage message = (ProtocolMessage)msg;
        switch ((int)message.getMessage().get(0)) {
            case MessageType.LOGIN:{
                parseLogin(message);
                break;
            }
            case MessageType.CREATE_ACCOUNT:{
                parseCreateAccount(message);
                break;
            }
            case MessageType.REQUEST_USERS:{
                parseUserRequest(message);
                break;
            }
            case MessageType.DELETE_USER: {
                parseDeleteUser(message);
                break;
            }
            case MessageType.DISCONNECT:{
                ctx.close();
                break;
            }
            case MessageType.REQUEST_BRIDGE:{
                parseBridgeRequest(message);
                break;
            }
            case MessageType.REQUEST_WATCHLIST:{
                parseWatchListRequest(message);
                break;
            }
            case MessageType.BRIDGE_STATUS_UPDATE:{
                parseBridgeStatusUpdate(message);
                break;
            }
            case MessageType.REPUTATION:{
                parseReputation(message);
                break;
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * Method which parses the incoming result of the create user query, and sends an int to the method which builds the alert
     * @param message
     */
    public void parseCreateAccount(ProtocolMessage message){
        Integer result = (Integer) message.getMessage().get(1);
        ActivityHandler.handler.checkCreateAccount(result);
    }

    /**
     * Method which sets the account details to the logged in account.
     * @param message
     */
    public void parseLogin(ProtocolMessage message){
        int[] returnMessage = (int[])message.getMessage().get(1);
        if(returnMessage == null) {
            Account.resetAccount();
        }
        else{
            Account.sendGcmToken(Account.getToken());
            Account.setId(returnMessage[0]);
            Account.setAccessLevel(returnMessage[1]);
            Account.requestBridges();
            ActivityHandler.handler.switchActivity(Menu.class);
        }
        ActivityHandler.handler.enableLogin();
    }

    /**
     * Method which passes an arraylist of all known users from the database to the app.
     * @param message message from the server containing the arraylist.
     */
    public void parseUserRequest(ProtocolMessage message){
        ArrayList<String> users = (ArrayList) message.getMessage().get(1);
        ActivityHandler.handler.getUsers(users);
    }

    /**
     * Method which parses the incoming result of the delete user query, and sends an int to the method which builds the alert
     * @param message
     */
    public void parseDeleteUser(ProtocolMessage message){
        Integer result = (Integer) message.getMessage().get(1);
        ActivityHandler.handler.checkUserDelete(result);
    }

    /**
     * Method which passes an arraylist of all known bridges from the database to the app
     * @param message message from the server containing the arraylist
     */
    public void parseBridgeRequest(ProtocolMessage message){
        ArrayList<String[]> bridgeList = (ArrayList)message.getMessage().get(1);

        for(int i = 0; i< bridgeList.size(); i++) {
            Bridge bridge = new Bridge(Integer.parseInt(bridgeList.get(i)[0]),
                    bridgeList.get(i)[1],
                    bridgeList.get(i)[2],
                    Double.parseDouble(bridgeList.get(i)[3]),
                    Double.parseDouble(bridgeList.get(i)[4]),
                    Boolean.parseBoolean(bridgeList.get(i)[5]));
            Account.bridgeMap.put(bridge.getId(), bridge);
        }
        //Bridge.handler.updateBridgeList();

        Account.mBridgeList = new ArrayList<>(Account.bridgeMap.values());
        HelperTools.updateBridgeDistance();
        Account.getWatchList();
    }

    /**
     * Method which passes an arraylist containing all bridges of the watchlist of the current logged in account
     * @param message message from the server containing the arraylist
     */
    public void parseWatchListRequest(ProtocolMessage message){
        ArrayList<String[]> watchList = (ArrayList)message.getMessage().get(1);

        for(int i = 0; i< watchList.size(); i++) {
            Bridge bridge = Account.bridgeMap.get(Integer.parseInt(watchList.get(i)[0]));
            Account.watchListMap.put(bridge.getId(), bridge);
        }

        Account.mWatchList = new ArrayList<>(Account.watchListMap.values());

        if(WatchList.handler != null)
            WatchList.handler.updateList();
    }

    /**
     * Method which regulates the update of the status of a given bridge
     * @param message message containing the bridge's id and its status
     */
    public void parseBridgeStatusUpdate(ProtocolMessage message){
        int bridgeId = (int) message.getMessage().get(1);
        boolean status = (boolean) message.getMessage().get(2);
        Account.bridgeMap.get(bridgeId).setOpen(status);

        if(eu.silvenia.bridgeballot.activity.menufragment.Bridge.handler != null)
            eu.silvenia.bridgeballot.activity.menufragment.Bridge.handler.updateList();
        if(WatchList.handler != null)
            WatchList.handler.updateList();
    }

    /**
     * Method which parses the reputation request (e.g. highers of lowers the reputation of a given user)
     * @param message message from the server containing the arraylist with reputation numbers
     */
    private void parseReputation(ProtocolMessage message) {
        ArrayList<Reputation> reputationList = new ArrayList<>();
        ArrayList<String[]> list = (ArrayList) message.getMessage().get(1);
        int bridgeId = 0;

        for(int i = 0; i < list.size(); i++){
            String[] clientRep = list.get(i);

            bridgeId = Integer.parseInt(clientRep[6]);

            Reputation reputation = new Reputation(Integer.parseInt(clientRep[0]),
                    Integer.parseInt(clientRep[1]),
                    clientRep[2],
                    Integer.parseInt(clientRep[3]),
                    new Date(Integer.parseInt(clientRep[4])),
                    Boolean.parseBoolean(clientRep[5]),
                    Integer.parseInt(clientRep[6]));
            reputationList.add(reputation);
        }

        Account.bridgeMap.get(bridgeId).repList.clear();
        Account.bridgeMap.get(bridgeId).repList.addAll(reputationList);
        ActivityHandler.handler.updateRepList();
    }

}