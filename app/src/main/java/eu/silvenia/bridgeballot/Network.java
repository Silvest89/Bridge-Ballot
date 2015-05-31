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
    }

    public final static class ReturnType {
        public static final int SUCCESS = 0;
        public static final int FAILURE = 1;
    }

    private static final int SERVERPORT = 21;
    private static final String SERVER_IP = "145.24.222.142";

    public Network(Account a){
        NetworkTask network = new NetworkTask(SERVER_IP, SERVERPORT, a);
        network.execute();
    }

    public class NetworkTask extends AsyncTask<Void, Void, Void> {

        String response = "";
        Account account;
        Socket socket;

        NetworkTask(String addr, int port, Account a) {
            account = a;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
                try {
                    socket = new Socket(SERVER_IP, SERVERPORT);
                    System.out.println("Connected");
                    //ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeInt(MessageType.LOGIN);
                    out.flush();
                    String[] loginDetails = {account.getUserName(), account.getPassword()};
                    System.out.println(loginDetails);
                    out.writeObject(loginDetails);
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    int returnType = in.readInt();
                    System.out.println(returnType);
                    //socket.close();

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}

