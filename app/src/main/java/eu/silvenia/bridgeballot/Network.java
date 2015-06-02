package eu.silvenia.bridgeballot;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
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
    }

    public final static class ReturnType {
        public static final int SUCCESS = 0;
        public static final int FAILURE = 1;
    }

    public static Socket socket;

    private static final int SERVERPORT = 21;
    private static final String SERVER_IP = "145.24.222.142";

    public void login(){
        LoginTask network = new LoginTask();
        network.execute();
    }
    public void sendToken(String token){
        SendTokenTask sendToken = new SendTokenTask(token);
        sendToken.execute();
    }
    public ArrayList requestBridge() throws ExecutionException, InterruptedException {
        RequestBridge network = new RequestBridge();
        ArrayList bridgeList = network.execute().get();
        return bridgeList;

    }

    public class LoginTask extends AsyncTask<Void, Void, Void> {

        String response = "";

        LoginTask() {
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                System.out.println("Connected");
                //ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                String[] loginDetails = { Account.getUserName(),  Account.getPassword()};

                out.writeInt(MessageType.LOGIN);
                out.writeObject(loginDetails);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                int returnType = in.readInt();
                System.out.println(returnType);
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
    public class SendTokenTask extends AsyncTask<Void, Void, Void>  {

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
    }

    public class RequestBridge extends AsyncTask<Void, Void, ArrayList> {


        @Override
        protected ArrayList doInBackground(Void... arg0) {
            try {
                socket = new Socket(SERVER_IP, SERVERPORT);
                System.out.println("Connected");
                //ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                out.writeInt(MessageType.BRIDGE_REQUEST);
                out.flush();

                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ArrayList bridgeList = (ArrayList) in.readObject();
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
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);
        }
    }
}


