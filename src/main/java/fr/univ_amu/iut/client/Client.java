package fr.univ_amu.iut.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class Client {
    private String hostname;
    private int port;
    private Socket socketClient;
    private BufferedWriter out;
    private BufferedReader in;
    private String message;

    public Client(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        socketClient = new Socket(hostname, port);
        out = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
    }

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
     * Send the message received from the server and if the server disconnected, return an alert
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
   public String receiveMessageFromServer() throws IOException, InterruptedException {
       if((message = in.readLine()) != null) {
            return message;
       }
       Alert serverError = new Alert(Alert.AlertType.ERROR, "Le serveur est déconnecté !");
       serverError.show();
       sleep(3000);
       close();
       Platform.exit();
       return null;
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
        socketClient.close();
    }
}


