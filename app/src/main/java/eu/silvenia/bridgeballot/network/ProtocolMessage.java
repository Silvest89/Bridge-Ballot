package eu.silvenia.bridgeballot.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for protocolmessages: messages send to the server and back to communicate
 */
public class ProtocolMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Object> protocolMessage = new ArrayList<>();

    /**
     * Constructor which initiates a new Protocolmessage
     * @param messageType the message type to be added to the message (e.g. LOGIN, REQUEST_BRIDGES etc.)
     */
    public ProtocolMessage(int messageType){
        protocolMessage.add(messageType);
    }

    /**
     * Method for adding additional object to the message before it's being send
     * @param object the object to be added.
     */
    public void add(Object object){
        protocolMessage.add(object);
    }

    /**
     * Method which gets the current message
     * @return an arraylist containing all objects of the message
     */
    public ArrayList getMessage(){
        return protocolMessage;
    }
}
