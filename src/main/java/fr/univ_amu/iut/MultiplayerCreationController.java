package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class MultiplayerCreationController extends QuestionController{
    @FXML
    private Label codePartie;
    private Client client;

    public MultiplayerCreationController() {
        client = Main.getClient();
    }

    /**
     * Send a message to the server to begin the multiplayer's session
     */
    public void sessionBegin(ActionEvent event) throws IOException, InterruptedException {
        client.sendMessageToServer("BEGIN");    // Send to the server that the host want to start the game by clicking on the 'Lancer' button
        if(client.receiveMessageFromServer().equals("CAN_JOIN_FLAG")) { // The host can join the multiplayer's session
            client.changePort(Integer.valueOf(client.receiveMessageFromServer()));  // Connect to the multiplayer session
        }
        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, "fxml/question.fxml");
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     */
    @FXML
    public void initialize() throws IOException, InterruptedException {
        if(client.receiveMessageFromServer().equals("CODE_FLAG")) {
            codePartie.setText(client.receiveMessageFromServer());
        }
    }
}