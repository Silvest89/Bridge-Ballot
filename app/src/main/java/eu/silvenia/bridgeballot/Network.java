package eu.silvenia.bridgeballot;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Jesse on 29-5-2015.
 */
public class Network {

    public final static class MessageType {
        public static final int LOGIN = 0;
        public static final int ADD_BRIDGE = 1;
        public static final int CLOSE_CONNECTION = 2;
        public static final int SEND_TOKEN = 3;
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
}


