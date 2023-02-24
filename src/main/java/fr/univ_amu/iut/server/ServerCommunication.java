package fr.univ_amu.iut.server;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.net.SocketFactory;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.security.Security;
import java.util.Arrays;

import javafx.util.Duration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Supports the communication with the server
 * @author LennyGonzales
 */
public class ServerCommunication {
    private SSLSocket socketClient;
    private Object object;
    private ObjectInputStream inObject;
    private ObjectOutputStream outObject;
    private boolean isRequested;
    private MessageListener messageListener;

    public ServerCommunication(String hostname, int port) throws IOException {
        try {
            Security.addProvider(new BouncyCastleProvider());
            System.setProperty("javax.net.ssl.trustStore", "myTrustStore.jts");
            System.setProperty("javax.net.ssl.trustStorePassword", "password"); //----to do
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socketClient = (SSLSocket) factory.createSocket(hostname, port);
            socketClient.setEnabledProtocols(new String[] { "TLSv1.3" });

            inObject = new ObjectInputStream(socketClient.getInputStream());
            outObject = new ObjectOutputStream(socketClient.getOutputStream());
            isRequested = true;

        } catch(ConnectException e) {
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "Échec de la connexion avec le serveur, veuillez réessayer ultérieurement.");
            connexionError.setOnCloseRequest(event -> Platform.exit());
            connexionError.showAndWait();
        }
    }

    /**
     * Set the listener
     * @param messageListener
     */
    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * Return the object received from the server
     * @return the object
     */
    public void startListening() {
        new Thread(() -> {
            while(isRequested) {
                try {
                    object = inObject.readObject();
                    messageListener.onMessageReceived(object);
                } catch (ClassNotFoundException | NotTheExpectedFlagException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
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
    public String receiveMessageFromServer() throws IOException, NotAStringException {
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
     * Close the streams and the socket
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void close() throws IOException {
        inObject.close();
        outObject.close();
        socketClient.close();
    }
}


