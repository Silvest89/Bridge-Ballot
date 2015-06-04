package eu.silvenia.bridgeballot;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jesse on 29-5-2015.
 */
public class Network {

    public final static class MessageType {
        public static final int LOGIN = 0;
        public static final int DISCONNECT = 1;
        public static final int SEND_TOKEN = 2;
        public static final int BRIDGE_REQUEST = 3;
        public static final int BRIDGE_ADD = 4;
        public static final int BRIDGE_DELETE = 5;
        public static final int CREATE_ACCOUNT = 7;
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

   /* public void sendToken(String token){
        SendTokenTask sendToken = new SendTokenTask(token);
        sendToken.execute();
    }*/

    public ArrayList requestBridge() {
        RequestBridge network = new RequestBridge();
        ArrayList bridgeList = null;
        try {
            bridgeList = network.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return bridgeList;

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

                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                String[] loginDetails = { username,  password, token};

                out.writeInt(MessageType.LOGIN);
                out.writeBoolean(isGooglePlus);
                out.writeObject(loginDetails);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                int returnType = in.readInt();

                if(returnType > 0) {
                    Account.setUserName(username);
                    Account.setGooglePlus(isGooglePlus);
                    Account.setId(returnType);
                    return true;
                }

                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

    public class RequestBridge extends AsyncTask<Void, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(Void... arg0) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                System.out.println("Request Bridge");
                //ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeInt(MessageType.BRIDGE_REQUEST);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Object object = in.readObject();
                //String[] loginDetails = (String[]) in.readObject();
                System.out.println(object);
                socket.close();
                //return bridgeList;
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
        protected void onPostExecute(ArrayList result) {
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
                socket = new Socket(SERVER_IP, SERVERPORT);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                String[] loginDetails = { username,  password};

                out.writeInt(MessageType.CREATE_ACCOUNT);
                out.writeObject(loginDetails);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
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
            }
            return -1;
        }
    }
}


