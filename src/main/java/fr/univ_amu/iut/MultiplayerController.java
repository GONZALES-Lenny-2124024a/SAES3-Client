package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller of the multiplayer's page
 */
public class MultiplayerController {
    @FXML
    private TextField codeInput;
    private Client client;
    private SceneController sceneController;

    public MultiplayerController() {
        client = Main.getClient();
        sceneController = new SceneController();
    }

    /**
     * Send the multiplayer join flag
     * Change the port to communicate with the multiplayer session's server
     * Switch to the loading page
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void joinSession() throws IOException, InterruptedException {
        sendJoinFlag(); // Send multiplayer session's code
        if(client.receiveMessageFromServer().equals("CODE_EXISTS_FLAG")) {  // Verify if the code is in the database
            client.changePort(Integer.valueOf(client.receiveMessageFromServer()));  // Change the port to communicate with the multiplayer session's server

            // Use to not run indefinitely the page (no crash page) until the host click on the 'Lancer' button
            if(client.receiveMessageFromServer().equals("PRESENCE_FLAG")) {   // To see if the server receive the connection request
                sceneController.switchTo("fxml/loading.fxml");
            }
        }
    }

    /**
     * Send multiplayer session's code
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendJoinFlag() throws IOException {
        client.sendMessageToServer("MULTIPLAYER_JOIN_FLAG");
        client.receiveMessageFromServer();
        client.sendMessageToServer(codeInput.getText());
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @throws IOException
     * @throws InterruptedException
     */
    public void creationSession() throws IOException {
        client.sendMessageToServer("MULTIPLAYER_CREATION_FLAG");
        sceneController.switchTo("fxml/multiplayerCreation.fxml");
    }

    /**
     * Switch to the menu's page
     * @throws IOException
     */
    public void switchToMenu() throws IOException {
        sceneController.switchTo("fxml/menu.fxml");
    }
}
