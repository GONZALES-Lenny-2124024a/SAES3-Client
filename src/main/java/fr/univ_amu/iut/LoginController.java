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
import javafx.scene.input.KeyCode;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Controller of the login's page
 * @author LennyGonzales
 */
public class LoginController extends Speech implements CommunicationController {
    private static final String DEFAULT_SPEECH = "Page Login. Entre ton email, appuie sur tab, entre ton mot de passe, et clique sur la touche entrer";
    @FXML
    private TextField mailTextField;
    @FXML
    private PasswordField passwordTextField;
    private final Communication communication;
    private final SceneController sceneController;
    public LoginController() throws IOException {
        if(Main.getCommunication() == null) {
            Main.setCommunication(new Communication()); // Start the communication with the server
        }
        this.communication = Main.getCommunication();
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
     * Initialize the interaction with the server to receive server message(s)
     */
    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch(message.getFlag()) {
                    case LOGIN_SUCCESSFULLY -> Platform.runLater(() -> {
                        try {
                            interruptThreadRunning();
                            communication.setMessageListener(null);
                            sceneController.switchTo("fxml/menu.fxml"); // Now, the user can access to the menu page
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    case LOGIN_NOT_SUCCESSFULLY -> Platform.runLater(() ->  {
                        Alert connexionError = new Alert(Alert.AlertType.ERROR, "Les identifiants fournis sont incorrects, veuillez réessayer ou créer votre compte sur notre site web : https://nwstories.alwaysdata.net");
                        speech("Les identifiants fournis sont incorrects, veuillez réessayer ou créer votre compte sur notre site web : https://nwstories.alwaysdata.net");
                        connexionError.show();
                    });
                    default -> throw new NotTheExpectedFlagException("LOGIN_SUCCESSFULLY or LOGIN_NOT_SUCCESSFULLY");
                }
            }
        };
        communication.setMessageListener(messageListener);
        if(!communication.getIsListening()) {
            communication.startListening();
        }
    }

    @FXML
    public void initialize() throws InterruptedException {
        initializeInteractionServer();
        initializeTextToSpeech(mailTextField.getParent(), DEFAULT_SPEECH);

        mailTextField.getParent().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {     // When the user pressed on enter, it sends the form
                try {
                    sendLogin();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
