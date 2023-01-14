package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

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
     * Verify if the login credentials are corrects or not
     * @return true - if the username and the password are corrects | else, false
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws NotTheExpectedFlagException Throw when the flag received isn't the expected flag | Print the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    public boolean verifyLogin(String mail, String password) throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        // Send the login credentials to the server
        serverCommunication.sendMessageToServer("LOGIN_FLAG");
        serverCommunication.sendMessageToServer(mail);
        serverCommunication.sendMessageToServer(password);

        // Receive the result from the server
        String message = serverCommunication.receiveMessageFromServer();
        if(!(message.equals("LOGIN_SUCCESSFULLY_FLAG")) && !(message.equals("LOGIN_NOT_SUCCESSFULLY_FLAG"))) {
            throw new NotTheExpectedFlagException("LOGIN_SUCCESSFULLY_FLAG or LOGIN_NOT_SUCCESSFULLY_FLAG");
        }
        return message.equals("LOGIN_SUCCESSFULLY_FLAG");
    }

    /**
     * Supports the user login
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     * @throws NotTheExpectedFlagException if the flag for the verification of the connexion isn't the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    public void serviceLogin() throws IOException, UrlOfTheNextPageIsNull, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        if(verifyLogin(mailTextField.getText(),passwordTextField.getText())) {
            mail = mailTextField.getText();  // Store the mail into a static variable for the multiplayer (send the mail to the host when the user join a multiplayer session)
            sceneController.switchTo("fxml/menu.fxml"); // Now, the user can access to the menu page
        }  else{    // print an alert error
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "The username and/or password are incorrect");
            connexionError.show();
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
}
