package fr.univ_amu.iut.communication;

import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.ConnectException;

/**
 * Supports the communication with the server
 * @author LennyGonzales
 */
public class Communication {
    private SSLSocket socketClient;
    private Object object;
    private ObjectInputStream inObject;
    private ObjectOutputStream outObject;
    private boolean isRequested;
    private MessageListener messageListener;
    private Thread threadListener;
    private boolean isListening = false;

    public Communication(String hostname, int port) throws IOException {
        try {
            System.setProperty("javax.net.ssl.keyStore", "keyStore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "Gyaz1ycgG-9bu");   // ----!!!
            System.setProperty("javax.net.ssl.trustStore", "trustStore.jts");
            System.setProperty("javax.net.ssl.trustStorePassword", "Gyaz1ycgG-9bu"); //----to do

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
        threadListener = new Thread(() -> {
            while(isRequested) {
                try {
                    object = inObject.readObject();
                    if(object instanceof CommunicationFormat) {
                        // stop until messageListener not null
                        while(messageListener == null) {
                            Thread.sleep(100);
                        }
                        messageListener.onMessageReceived((CommunicationFormat) object);
                    }
                } catch (ClassNotFoundException | NotTheExpectedFlagException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        isListening = true;
        threadListener.start();
    }

    public boolean getIsListening() {
        return isListening;
    }

    /**
     * Send a String to the server
     * @param message (flag + content) to send to the server
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void sendMessage(CommunicationFormat message) throws IOException {
        outObject.writeObject(message);
        outObject.flush();
    }


    /**
     * Close the streams and the socket
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void close() throws IOException {
        threadListener.interrupt();
        inObject.close();
        outObject.close();
        socketClient.close();
    }
}


