package fr.univ_amu.iut.client;

import java.io.*;
import java.net.Socket;

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
     * This method close the socket
     * @throws IOException
     */
    public void close() throws IOException {
        in.close();
        out.close();
        socketClient.close();
    }
}


