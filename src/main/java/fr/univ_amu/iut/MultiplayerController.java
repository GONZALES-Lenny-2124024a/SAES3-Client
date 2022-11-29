package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MultiplayerController {
    @FXML
    private TextField codeInput;
    private Client client;

    public MultiplayerController() {
        client = Main.getClient();
    }

    /**
     * Send the multiplayer join flag
     * Change the port to communicate with the multiplayer session's server
     * Switch to the loading page
     *
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void joinSession(ActionEvent event) throws IOException, InterruptedException {
        // Send the multiplayer join flag
        client.sendMessageToServer("MULTIPLAYER_JOIN_FLAG");
        client.receiveMessageFromServer();
        client.sendMessageToServer(codeInput.getText());

        client.changePort(Integer.valueOf(client.receiveMessageFromServer()));  // Change the port to communicate with the multiplayer session's server

        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, "fxml/question.fxml");   // Switch to the question's page
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void creationSession(ActionEvent event) throws IOException, InterruptedException {
        client.sendMessageToServer("MULTIPLAYER_CREATION_FLAG");
        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, "fxml/multiplayerCreation.fxml");
    }

    /**
     * Switch to the menu's page
     * @param event
     * @throws IOException
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, "fxml/menu.fxml");
    }
}
