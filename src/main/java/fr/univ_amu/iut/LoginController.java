package fr.univ_amu.iut;

import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

/**
 * Controller of the login's page
 */
public class LoginController {
    @FXML
    private TextField mailTextField;
    @FXML
    private PasswordField passwordTextField;
    private final ServerCommunication serverCommunication;
    private String message;
    private static String mail;

    private final SceneController sceneController;
    public LoginController() {
        serverCommunication = Main.getServerCommunication();  // Get the connection with the server
        sceneController = new SceneController();
    }


    /**
     * Supports the login service
     * @return if the username and the password are corrects
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public boolean verifyLogin(String mail, String password) throws IOException, NotTheExpectedFlagException {
        serverCommunication.sendMessageToServer("LOGIN_FLAG");
        serverCommunication.sendMessageToServer(mail);
        serverCommunication.sendMessageToServer(password);
        message = serverCommunication.receiveMessageFromServer();

        if(!(message.equals("LOGIN_SUCCESSFULLY_FLAG")) && !(message.equals("LOGIN_NOT_SUCCESSFULLY_FLAG"))) {
            throw new NotTheExpectedFlagException("LOGIN_SUCCESSFULLY_FLAG or LOGIN_NOT_SUCCESSFULLY_FLAG");
        }
        return message.equals("LOGIN_SUCCESSFULLY_FLAG");
    }

    /**
     * Supports the login of the user
     * @param event of the button actioned
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void serviceLogin(ActionEvent event) throws IOException, UrlOfTheNextPageIsNull, NotTheExpectedFlagException {
        if(verifyLogin(mailTextField.getText(),passwordTextField.getText())) {
            mail = mailTextField.getText();  // Store the mail into a static variable for the multiplayer (send the mail to the host when the user join a multiplayer session)
            //Get the name of the file
            Node node = (Node) event.getSource() ;
            String nameNextPage = (String) node.getUserData();
            sceneController.switchTo(nameNextPage);
        }  else{
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "The username and/or password are incorrect");
            connexionError.show();
        }
    }

    public static String getMail() { return mail;}
}
