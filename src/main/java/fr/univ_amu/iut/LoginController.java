package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
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
    private TextField mail;
    @FXML
    private PasswordField password;
    private Client client;
    private String message;

    private SceneController sceneController;
    public LoginController() {
        client = Main.getClient();  // Get the connection with the server
        sceneController = new SceneController();
    }


    /**
     * Supports the login service
     * @return if the username and the password are corrects
     * @throws IOException
     */
    public boolean verifyLogin(String mail, String password) throws IOException {
        client.sendMessageToServer("LOGIN_FLAG");
        client.sendMessageToServer(mail);
        client.sendMessageToServer(password);
        message = client.receiveMessageFromServer();
        return message.equals("[+] LOGIN !");
    }

    /**
     * Supports the login of the user
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void serviceLogin(ActionEvent event) throws IOException {
        if(verifyLogin(mail.getText(),password.getText())) {
            //Get the name of the file
            Node node = (Node) event.getSource() ;
            String nameNextPage = (String) node.getUserData();
            sceneController.switchTo(nameNextPage);
        }  else{
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "L'identifiant ou le mot de passe saisi est incorrect !");
            connexionError.show();
        }
    }


}
