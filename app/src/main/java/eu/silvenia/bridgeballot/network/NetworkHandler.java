package eu.silvenia.bridgeballot.network;

/**
 * Created by Johnnie Ho on 10-6-2015.
 */
import java.util.ArrayList;

import eu.silvenia.bridgeballot.Account;
import eu.silvenia.bridgeballot.BridgeFragment;
import eu.silvenia.bridgeballot.MainActivity;
import eu.silvenia.bridgeballot.MenuActivity;
import eu.silvenia.bridgeballot.WatchListFragment;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Handler implementation for the object echo client.  It initiates the
 * ping-pong traffic between the object echo client and server by sending the
 * first message to the server.
 */
public class NetworkHandler extends ChannelHandlerAdapter {

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Echo back the received object to the server.

        ProtocolMessage message = (ProtocolMessage)msg;
        switch ((int)message.getMessage().get(0)) {
            case MessageType.LOGIN:{
                parseLogin(message);
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

    public void parseLogin(ProtocolMessage message){
        int[] returnMessage = (int[])message.getMessage().get(1);
        if(returnMessage == null) {
            Account.resetAccount();
            //Account.getChannel().close();
        }
        else{
            Account.sendGcmToken(Account.getToken());
            Account.setId(returnMessage[0]);
            Account.setAccessLevel(returnMessage[1]);
            Account.requestBridges();
            MainActivity.handler.switchActivity(MenuActivity.class);

        }
        MainActivity.handler.enableLogin();
    }

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
        //BridgeFragment.handler.updateBridgeList();

        BridgeFragment.mBridges = new ArrayList<>(Account.bridgeMap.values());
    }

    public void parseWatchListRequest(ProtocolMessage message){
        ArrayList<String[]> watchList = (ArrayList)message.getMessage().get(1);

        System.out.println(watchList);
        for(int i = 0; i< watchList.size(); i++) {
            Bridge bridge = Account.bridgeMap.get(Integer.parseInt(watchList.get(i)[0]));
            Account.watchListMap.put(bridge.getId(), bridge);
        }

        WatchListFragment.mBridges = new ArrayList<>(Account.watchListMap.values());
        WatchListFragment.handler.updateWatchList();
    }

}