package fr.univ_amu.iut.client;

import fr.univ_amu.iut.LoginController;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    private String hostname;
    private int port;
    private Socket sockClient;
    private BufferedWriter out;
    private BufferedReader in;
    private String message;

    public Client(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;
        sockClient = new Socket(hostname,port);
        out = new BufferedWriter(new OutputStreamWriter(sockClient.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
    }

    /**
     * Supports the login service
     * @throws IOException
     */
    public void serviceLogin(LoginController loginController) throws IOException {
        out.write("LOGIN_FLAG");
        out.newLine();
        out.flush();

        HashMap<String,String> credentials = loginController.getCredentials();
        out.write(credentials.get("mail"));
        out.newLine();
        out.write(credentials.get("password"));
        out.newLine();
        out.flush();

        if((message = in.readLine()) != null) {
            if(message.equals("[+] LOGIN")) {
                System.out.println(message);
                // loginController.switchPage();
            }
            System.out.println(message);
            // loginController.printError();
        } else {
            close();
        }
    }

    /**
     * This method close the socket
     * @throws IOException
     */
    private void close() throws IOException {
        in.close();
        out.close();
        sockClient.close();
    }
}


