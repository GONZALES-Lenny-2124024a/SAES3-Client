package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.communication.ServerCommunication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Controller of the login's page
 * @author LennyGonzales
 */
public class LoginController {
    @FXML
    private TextField mailTextField;
    @FXML
    private PasswordField passwordTextField;
    private final ServerCommunication serverCommunication;
    private static String mail;

    private final SceneController sceneController;
    public LoginController() {
        serverCommunication = Main.getServerCommunication();  // Get the connection with the server
        sceneController = new SceneController();
    }

    /**
     * Send the login credentials to the server
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void sendLogin() throws IOException {
        serverCommunication.sendMessage(Flags.LOGIN, Arrays.asList(mailTextField.getText(), passwordTextField.getText()));
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
            public void onMessageReceived(Object message) throws NotTheExpectedFlagException {
                if (message instanceof HashMap) {
                    Iterator it = ((HashMap<Flags, String>) message).entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Flags, String> entry = (Map.Entry)it.next(); // Get element
                        // Use element
                        switch(entry.getKey()) {
                            case LOGIN_SUCCESSFULLY -> Platform.runLater(() -> {
                                try {
                                    serverCommunication.setMessageListener(null);
                                    loginSuccessful();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                            case LOGIN_NOT_SUCCESSFULLY -> Platform.runLater(() -> loginFailed());
                            default -> throw new NotTheExpectedFlagException("LOGIN_SUCCESSFULLY or LOGIN_NOT_SUCCESSFULLY");
                        }

                        // Remove Element
                        it.remove();
                    }
                }
            }
        };
        serverCommunication.setMessageListener(messageListener);
        serverCommunication.startListening();
    }

    public void loginSuccessful() throws Exception {
        mail = mailTextField.getText();  // Store the mail into a static variable for the multiplayer (send the mail to the host when the user join a multiplayer session)
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
