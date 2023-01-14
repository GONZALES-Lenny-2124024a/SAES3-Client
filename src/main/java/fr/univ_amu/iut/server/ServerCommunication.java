package fr.univ_amu.iut.server;

import fr.univ_amu.iut.exceptions.NotAStringException;

import java.io.*;
import java.net.Socket;

/**
 * Supports the communication with the server
 * @author LennyGonzales
 */
public class ServerCommunication {
    private final Socket socketClient;
    private final BufferedReader in;
    private Object object;
    private final ObjectInputStream inObject;
    private final ObjectOutputStream outObject;

    public ServerCommunication(String hostname, int port) throws IOException {
        socketClient = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        inObject = new ObjectInputStream(socketClient.getInputStream());
        outObject = new ObjectOutputStream(socketClient.getOutputStream());
    }


    /**
     * Send a String to the server
     * @param message to send to the server
     * @throws IOException if the communication with the server is closed or didn't go well
     */
   public void sendMessageToServer(String message) throws IOException {
        outObject.writeObject(message);
        outObject.flush();
   }

    /**
     * Return the message/String received from the server
     * @return the message or null if the server disconnected
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws NotAStringException throw when the Object received isn't a String
     * @throws ClassNotFoundException if the object class not found
     */
   public String receiveMessageFromServer() throws IOException, ClassNotFoundException, NotAStringException {
       try {
           if((object = inObject.readObject()) instanceof String) {
               return object.toString();
           }
           throw new NotAStringException(object);
       } catch (EOFException | ClassNotFoundException e) {
           close();
           e.printStackTrace();
       }
       return null;
   }

    /**
     * Return the object received from the server
     * @return the object or null if the server disconnected
     * @throws IOException if the communication with the server is closed or didn't go well
     */
   public Object receiveObjectFromServer() throws IOException {
       try {
           object = inObject.readObject();
           return object;
       } catch (EOFException | ClassNotFoundException e) {
           close();
           e.printStackTrace();
       }
       return null;
   }

    /**
     * Return true if the server sent a message to the client
     * @return true - if the server sent a message | else, false
     * @throws IOException if the communication with the server is closed or didn't go well
     */
   public boolean isReceiveMessageFromServer() throws IOException {
       return in.ready();
   }

    /**
     * Close the streams and the socket
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void close() throws IOException {
        in.close();
        inObject.close();
        outObject.close();
        socketClient.close();
    }
}


