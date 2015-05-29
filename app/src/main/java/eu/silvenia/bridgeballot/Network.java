package eu.silvenia.bridgeballot;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Jesse on 29-5-2015.
 */
public class Network {

    private static Socket socket;
    private static final int SERVERPORT = 21;
    private static final String SERVER_IP = "145.24.222.142";

    public void run() {
        MyClientTask client = new MyClientTask(SERVER_IP, SERVERPORT);
        client.execute();
    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {
                System.out.println("Connected");
                socket = new Socket(SERVER_IP, SERVERPORT);
                BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
                PrintWriter out = new PrintWriter( socket.getOutputStream() );
                String s = in.readLine();
                if (s != null && s.length() > 0){
                    System.out.println(s);
                }


            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println(response);
            super.onPostExecute(result);
        }
    }
}