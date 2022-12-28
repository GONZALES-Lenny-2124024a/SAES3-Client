package fr.univ_amu.iut.client;

import fr.univ_amu.iut.SceneController;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Supports interactions Client-Server
 */
public class Client {
    private String hostname;
    private int port;
    private Socket socketClient;
    private BufferedWriter out;
    private BufferedReader in;
    private String message;
    private Object object;
    private ObjectInputStream inObject;

    public Client(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        socketClient = new Socket(hostname, port);
        out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
    }

    /**
     * Get the socket of the user/client
     * @return
     */
    public Socket getSocketClient() {
        return socketClient;
    }

    /**
     * Send a String to the server
     * @param message
     * @throws IOException
     */
   public void sendMessageToServer(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
   }

    /**
     * Return the message received from the server
     * @return the message or null if the server disconnected
     * @throws IOException
     * @throws InterruptedException
     */
   public String receiveMessageFromServer() throws IOException {
       if((message = in.readLine()) != null) {
            return message;
       }
       close();
       return null;
   }


    /**
     * Return the object received from the server
     * @return the object or null if the server disconnected
     * @throws IOException
     * @throws ClassNotFoundException
     */
   public Object receiveObjectFromServer() throws IOException, ClassNotFoundException {
       inObject = new ObjectInputStream(socketClient.getInputStream()); // We can't instantiate in the constructor because the application don't run
       if ((object = inObject.readObject()) != null) {
           return object;
       }
       close();
       return null;
   }


    /**
     * Return true if the server sent a message to the client
     * @return
     * @throws IOException
     */
   public boolean isReceiveMessageFromServer() throws IOException {
       return in.ready();
   }

    /**
     * Change the port
     * @param newPort
     * @throws IOException
     */
   public void changePort(int newPort) throws IOException {
       socketClient = new Socket(hostname, newPort);
       out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
       in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
   }

    /**
     * Return the port of the main server
     * @return
     */
   public int getPort() {
       return port;
   }

    /**
     * This method close the socket
     * @throws IOException
     */
    public void close() throws IOException {
        in.close();
        out.close();
        inObject.close();
        socketClient.close();
        SceneController sceneController = new SceneController();
        Stage stage = sceneController.getStage();
        stage.close();
    }
}


