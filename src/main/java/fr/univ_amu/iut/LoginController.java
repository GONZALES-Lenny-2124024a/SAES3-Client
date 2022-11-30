package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;

public class LoginController{
    @FXML
    private TextField mail;
    @FXML
    private PasswordField password;
    @FXML
    private Button submit;
    private Client client;

    private BufferedWriter out;
    private BufferedReader in;
    private String message;

    private SceneController sceneController;
    public LoginController() throws IOException {
        client = Main.getClient();  // Get the connection with the server
        sceneController = new SceneController();
        out = new BufferedWriter(new OutputStreamWriter(client.getSocketClient().getOutputStream()));
        in = new BufferedReader(new InputStreamReader(client.getSocketClient().getInputStream()));
    }


    /**
     * Supports the login service
     * @return if the username and the password are corrects
     * @throws IOException
     */
    public boolean verifyLogin(String mail, String password) throws IOException {
        out.write("LOGIN_FLAG");
        out.newLine();
        out.flush();

        out.write(mail);
        out.newLine();
        out.write(password);
        out.newLine();
        out.flush();

        if((message = in.readLine()) != null) {
            return (message.equals("[+] LOGIN !")) ? true : false;
        }
        client.close();
        return false;
    }

    @FXML
    public void serviceLogin(ActionEvent event) throws IOException {
        if(verifyLogin(mail.getText(),password.getText())) {
            //Get the name of the file
            Node node = (Node) event.getSource() ;
            String nameNextPage = (String) node.getUserData();
            sceneController.switchTo(event, nameNextPage);
        }  else{
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "L'identifiant ou le mot de passe saisi est incorrect !");
            connexionError.show();
        }
    }


}
