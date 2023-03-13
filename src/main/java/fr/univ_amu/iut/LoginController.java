package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Controller of the login's page
 * @author LennyGonzales
 */
public class LoginController implements DefaultController{
    @FXML
    private TextField mailTextField;
    @FXML
    private PasswordField passwordTextField;
    private final Communication communication;
    private static String mail;

    private final SceneController sceneController;
    public LoginController() {
        communication = Main.getCommunication();  // Get the connection with the server
        sceneController = new SceneController();
    }

    /**
     * Send the login credentials to the server
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void sendLogin() throws IOException {
        communication.sendMessage(new CommunicationFormat(Flags.LOGIN, Arrays.asList(mailTextField.getText(), encryptPassword(passwordTextField.getText()))));
    }

    /**
     * Encrypt the password with SHA512 algorithm
     * @param str the String to hash
     * @return hashtext the String hashed
     * @throws RuntimeException if SHA-512 algorithm is not found
     */
    public String encryptPassword(String str)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");    // getInstance() is called with the SHA-512 algorithm

            // To calculate the message digest of the input string
            byte[] messageDigest = md.digest(str.getBytes());   // Returned as a byte array
            BigInteger no = new BigInteger(1, messageDigest);   // Convert bytes array into signum representation
            StringBuilder hashtext = new StringBuilder(no.toString(16));    // Convert message summary to hexadecimal value

            // Add the previous 0 to get the 32-bit.
            while (hashtext.length() < 32) {
                hashtext.insert(0,'0');
            }
            return hashtext.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the mail of the user
     * @return the mail of the user
     */
    public static String getMail() { return mail;}

    /**
     * Set the mail of the user
     * @param newMail the new mail of the user
     */
    public static void setMail(String newMail) {
        mail = newMail;
    }


    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch(message.getFlag()) {
                    case LOGIN_SUCCESSFULLY -> Platform.runLater(() -> {
                        try {
                            communication.setMessageListener(null);
                            loginSuccessful();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    case LOGIN_NOT_SUCCESSFULLY -> Platform.runLater(() -> loginFailed());
                    default -> throw new NotTheExpectedFlagException("LOGIN_SUCCESSFULLY or LOGIN_NOT_SUCCESSFULLY");
                }
            }
        };
        communication.setMessageListener(messageListener);
        if(!communication.getIsListening()) {
            communication.startListening();
        }
    }

    public void loginSuccessful() throws Exception {
        mail = mailTextField.getText();  // Store the mail into a static variable for the multiplayer (send the mail to the host when the user join a multiplayer session)
        communication.setMessageListener(null);
        sceneController.switchTo("fxml/menu.fxml"); // Now, the user can access to the menu page
    }

    public void loginFailed() {
        Alert connexionError = new Alert(Alert.AlertType.ERROR, "Les identifiants fournis sont incorrects, veuillez réessayer ou créer votre compte sur notre site web : https://nwstories.alwaysdata.net");
        connexionError.show();
    }

    @FXML
    public void initialize() {
        initializeInteractionServer();
    }
}
