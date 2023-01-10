package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
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
        serverCommunication = Main.getServerCommunication();
        sceneController = new SceneController();
    }

    /**
     * Send the multiplayer join flag, the multiplayer session code and the email of the user
     * Switch to the loading page
     *
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void joinSession() throws IOException, UrlOfTheNextPageIsNull {
        serverCommunication.sendMessageToServer("MULTIPLAYER_JOIN_FLAG");
        serverCommunication.sendMessageToServer(codeInput.getText());   // Get the multiplayer session code
        serverCommunication.sendMessageToServer(LoginController.getMail());
        sceneController.switchTo("fxml/loading.fxml");
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws ClassNotFoundException if the object class not found
     */
    public void creationSession() throws IOException {
        serverCommunication.sendMessageToServer("MULTIPLAYER_CREATION_FLAG");
        ModulesPage modulesController = new ModulesPage("fxml/multiplayerCreation.fxml");
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
