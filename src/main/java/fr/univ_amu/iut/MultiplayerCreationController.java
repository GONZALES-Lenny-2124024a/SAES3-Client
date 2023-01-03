package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller of the multiplayer session's creation page
 */
public class MultiplayerCreationController extends QuestionController{
    @FXML
    private TextField codeSession;
    private final Client client;
    private final SceneController sceneController;


    public MultiplayerCreationController() {
        client = Main.getClient();
        sceneController = new SceneController();
    }

    /**
     * Send a message to the server to begin the multiplayer's session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sessionBegin() throws IOException {
        client.sendMessageToServer("BEGIN");    // Send to the server that the host want to start the game by clicking on the 'Lancer' button
        if(client.receiveMessageFromServer().equals("CAN_JOIN_FLAG")) { // The host can join the multiplayer's session
            client.changePort(Integer.parseInt(client.receiveMessageFromServer()));  // Connect to the multiplayer session
        }
        sceneController.switchTo("fxml/question.fxml");
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    @FXML
    public void initialize() throws IOException {
        if(client.receiveMessageFromServer().equals("CODE_FLAG")) {
            codeSession.setText(client.receiveMessageFromServer());
        }
    }
}
