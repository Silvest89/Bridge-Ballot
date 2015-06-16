package eu.silvenia.bridgeballot;

import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import eu.silvenia.bridgeballot.network.Bridge;

/**
 * Created by Jesse on 29-5-2015.
 */
public class Network {

    public final static class MessageType {
        public static final int LOGIN = 0;
        public static final int DISCONNECT = 1;
        public static final int SEND_TOKEN = 2;

        public static final int CREATE_ACCOUNT = 5;
        public static final int REQUEST_USERS = 6;
        public static final int DELETE_USER = 7;

        public static final int BRIDGE_WATCHLIST_ADD = 10;
        public static final int BRIDGE_ON_WATCHLIST = 11;
        public static final int BRIDGE_REQUEST = 12;
        public static final int BRIDGE_ADD = 13;
        public static final int BRIDGE_DELETE = 14;
        public static final int BRIDGE_UPDATE = 15;

    }

    public final static class ReturnType {
        public static final int SUCCESS = 0;
        public static final int FAILURE = 1;
        public static final int FAILURE_NAME_NOT_UNIQUE = 2;
    }

    public static Socket socket;

    private static final int SERVERPORT = 21;
    private static final String SERVER_IP = "145.24.222.142";

    public boolean login(String username, String password, boolean isGooglePlus, String token){
        LoginTask network = new LoginTask(username, password, isGooglePlus, token);
        try {
            return network.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer createAccount(String username, String password) throws ExecutionException, InterruptedException {
        CreateAccountTask createAccountTask = new CreateAccountTask(username, password);
        Integer result = createAccountTask.execute().get();
        return result;
    }

    public Integer sendBridgeToWatchlist(int bridge_id, int username_id) throws ExecutionException, InterruptedException {
        BridgeToWatchlist bridgeToWatchlist = new BridgeToWatchlist(bridge_id, username_id);
        Integer result = bridgeToWatchlist.execute().get();
        return result;
    }

    public ArrayList<String> requestUsers() throws ExecutionException, InterruptedException {
        GetUserTask getUserTask = new GetUserTask();
        ArrayList<String> result = getUserTask.execute().get();
        return result;
    }

    public Integer deleteUser(String user) throws ExecutionException, InterruptedException {
        DeleteUserTask deleteUserTask = new DeleteUserTask(user);
        Integer result = deleteUserTask.execute().get();
        return result;
    }

   /* public void sendToken(String token){
        SendTokenTask sendToken = new SendTokenTask(token);
        sendToken.execute();
    }*/

    public HashMap<Integer, Bridge> requestBridge() {
        RequestBridge network = new RequestBridge();
        HashMap<Integer, Bridge> bridgeList = null;
        try {
            bridgeList = network.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return bridgeList;

    }

    public HashMap<Integer, Bridge> requestWatchlist(int username_id) {
        RequestWatchlist requestWatchlist = new RequestWatchlist(username_id);
        HashMap<Integer, Bridge> bridgeList = null;
        try {
            bridgeList = requestWatchlist.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return bridgeList;

    }

    public void updateBridgeList(int id){
        UpdateBridgeListTask task = new UpdateBridgeListTask(id);
        task.execute();
    }

    public class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private String username;
        private String password;
        private boolean isGooglePlus;
        private String token;

        LoginTask(String username, String password, boolean isGooglePlus, String token) {
            this.username = username;
            this.password = password;
            this.isGooglePlus = isGooglePlus;
            this.token = token;
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);

                System.out.println("LoginTask");
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(password.getBytes("UTF-8"));
                byte[] hash = md.digest();

                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String[] loginDetails = { username,  Base64.encodeToString(hash, Base64.DEFAULT), token};

                out.writeInt(MessageType.LOGIN);
                out.writeBoolean(isGooglePlus);
                out.writeObject(loginDetails);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                int[] returnType = (int[]) in.readObject();

                if(returnType != null) {
                    Account.setUserName(username);
                    Account.setGooglePlus(isGooglePlus);
                    Account.setId(returnType[0]);
                    Account.setAccessLevel(returnType[1]);
                    return true;
                }

                //socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
    }
  /*  public class SendTokenTask extends AsyncTask<Void, Void, Void>  {

        String token;
        public SendTokenTask(String token){
            this.token = token;
        }

        @Override
        protected Void doInBackground(Void...arg0){
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                System.out.println(token);
                out.writeInt(MessageType.SEND_TOKEN);
                out.writeUTF(token);
                out.flush();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return null;}
    }*/

    public class RequestBridge extends AsyncTask<Void, Void, HashMap<Integer, Bridge>> {

        @Override
        protected HashMap<Integer, Bridge> doInBackground(Void... arg0) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                Object object;

                System.out.println("Request Bridge");
                //ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                out.writeInt(MessageType.BRIDGE_REQUEST);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

                HashMap<Integer, Bridge> bridgeList = (HashMap)in.readObject();
                System.out.println(bridgeList);
                socket.close();
                return bridgeList;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, Bridge> result) {
            super.onPostExecute(result);
        }
    }

    public class CreateAccountTask extends AsyncTask<Void, Void, Integer>{
        private String username;
        private String password;

        public CreateAccountTask(String username, String password){
            this.username = username;
            this.password = password;
        }

        @Override
        protected Integer doInBackground(Void... args){
            try {
                System.out.println("LoginTask");
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(password.getBytes("UTF-8"));
                byte[] hash = md.digest();

                socket = new Socket(SERVER_IP, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                String[] loginDetails = { username,  Base64.encodeToString(hash, Base64.DEFAULT)};

                out.writeInt(MessageType.CREATE_ACCOUNT);
                out.writeObject(loginDetails);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                if(in.readInt() == ReturnType.SUCCESS){
                    socket.close();
                    return ReturnType.SUCCESS;
                }

                else {
                    socket.close();
                    return ReturnType.FAILURE_NAME_NOT_UNIQUE;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public class BridgeToWatchlist extends AsyncTask<Void, Void, Integer> {

        private int bridge_id;
        private int username_id;

        BridgeToWatchlist(int bridge_id, int username_id) {
            this.bridge_id = bridge_id;
            this.username_id = username_id;
        }

        @Override
        protected Integer doInBackground(Void... arg0) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);

                System.out.println("Sending bridge to watchlist");

                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                int[] watchlistDetails = { bridge_id, username_id };

                out.writeInt(MessageType.BRIDGE_WATCHLIST_ADD);
                out.writeObject(watchlistDetails);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                int returnType = in.readInt();

                if(returnType == ReturnType.SUCCESS) {
                    socket.close();
                    return ReturnType.SUCCESS;
                }
                else{
                    socket.close();
                    return ReturnType.FAILURE;
                }

                //socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
        }
    }

    public class RequestWatchlist extends AsyncTask<Void, Void, HashMap<Integer, Bridge>> {

        int username_id;

        public RequestWatchlist(int username_id){
            this.username_id = username_id;
        }
        @Override
        protected HashMap<Integer, Bridge> doInBackground(Void... arg0) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                Object object;

                System.out.println("Request Watchlist-Bridges");
                //ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                out.writeInt(MessageType.BRIDGE_ON_WATCHLIST);
                out.writeInt(username_id);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

                HashMap<Integer, Bridge> watchList = (HashMap)in.readObject();
                System.out.println(watchList);
                socket.close();
                return watchList;

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, Bridge> result) {
            super.onPostExecute(result);
        }
    }

    public class GetUserTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                out.writeInt(MessageType.REQUEST_USERS);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

                ArrayList<String> result = (ArrayList) in.readObject();

                return result;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class DeleteUserTask extends AsyncTask <Void, Void, Integer> {
        String user;

        public DeleteUserTask(String user){
            this.user = user;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                out.writeInt(MessageType.DELETE_USER);
                out.writeUTF(user);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

                int returnType = in.readInt();

                if (returnType == ReturnType.SUCCESS){
                    socket.close();
                    return ReturnType.SUCCESS;
                }

                else {
                    socket.close();
                    return ReturnType.FAILURE;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        }

    }

    public class UpdateBridgeListTask extends AsyncTask<Void, Void, Void> {
        int id;

        public UpdateBridgeListTask(int id){
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                out.writeInt(MessageType.BRIDGE_UPDATE);
                out.writeInt(id);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
