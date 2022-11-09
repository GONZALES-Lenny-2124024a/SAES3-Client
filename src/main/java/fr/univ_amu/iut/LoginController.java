package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.HashMap;

public class LoginController {
    @FXML
    private TextField mail;
    @FXML
    private PasswordField password;
    @FXML
    private Button submit;
    private Client client;
    public LoginController() {
        client = Main.getClient();  // Get the connection with the server
    }

    @FXML
    public void serviceLogin() throws IOException {
        client.serviceLogin(this);
    }

    /**
     * Get the credentials in the mail and password's fields
     */
    public HashMap<String,String> getCredentials() {
        HashMap<String,String> credentials = new HashMap<>();
        credentials.put("mail", mail.getText());
        credentials.put("password", password.getText());
        return credentials;
    }
}
