package fr.univ_amu.iut;

import fr.univ_amu.iut.client.ServerCommunication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller of the multiplayer's page
 */
public class MultiplayerController {
    @FXML
    private TextField codeInput;
    private final ServerCommunication serverCommunication;
    private final SceneController sceneController;

    public MultiplayerController() {
        serverCommunication = Main.getClient();
        sceneController = new SceneController();
    }

    /**
     * Send the multiplayer join flag
     * Change the port to communicate with the multiplayer session's server
     * Switch to the loading page
     *
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void joinSession() throws IOException, UrlOfTheNextPageIsNull {
        sendJoinFlag(); // Send multiplayer session's code
        if(serverCommunication.receiveMessageFromServer().equals("CODE_EXISTS_FLAG")) {  // Verify if the code is in the database
            serverCommunication.changePort(Integer.parseInt(serverCommunication.receiveMessageFromServer()));  // Change the port to communicate with the multiplayer session's server

            // Use to not run indefinitely the page (no crash page) until the host click on the 'Lancer' button
            if(serverCommunication.receiveMessageFromServer().equals("PRESENCE_FLAG")) {   // To see if the server receive the connection request
                serverCommunication.sendMessageToServer(LoginController.getMail());
                sceneController.switchTo("fxml/loading.fxml");
            }
        }
    }

    /**
     * Send multiplayer session's code
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendJoinFlag() throws IOException {
        serverCommunication.sendMessageToServer("MULTIPLAYER_JOIN_FLAG");
        serverCommunication.receiveMessageFromServer();
        serverCommunication.sendMessageToServer(codeInput.getText());
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws ClassNotFoundException if the object class not found
     */
    public void creationSession() throws IOException, ClassNotFoundException {
        serverCommunication.sendMessageToServer("MULTIPLAYER_CREATION_FLAG");
        ModulesController modulesController = new ModulesController("fxml/multiplayerCreation.fxml");
        modulesController.initialize();
    }

    /**
     * Switch to the menu's page
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void switchToMenu() throws IOException, UrlOfTheNextPageIsNull {
        sceneController.switchTo("fxml/menu.fxml");
    }
}
