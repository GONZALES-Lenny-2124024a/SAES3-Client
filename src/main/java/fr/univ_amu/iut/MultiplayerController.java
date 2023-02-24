package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller of the multiplayer's page
 * @author LennyGonzales
 */
public class MultiplayerController {
    @FXML
    private TextField codeInput;
    private final Communication communication;
    private final SceneController sceneController;

    public MultiplayerController() {
        communication = Main.getCommunication();
        sceneController = new SceneController();
    }

    /**
     * Send the multiplayer join flag, the multiplayer session code and the email of the user
     * Switch to the loading page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull Throw when the url of the next fxml page is null
     * @throws NotAStringException Throw when the object received from the server isn't a string
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     */
    public void joinSession() throws IOException, UrlOfTheNextPageIsNull, NotAStringException, ClassNotFoundException, InterruptedException {
        communication.sendMessageToServer("MULTIPLAYER_JOIN_FLAG");
        communication.sendMessageToServer(codeInput.getText());   // Get the multiplayer session code

        if((communication.receiveMessageFromServer()).equals("SESSION_EXISTS_FLAG")) {
            communication.sendMessageToServer(LoginController.getMail());
            sceneController.switchTo("fxml/loading.fxml");
        } else {
            Alert joinSessionError = new Alert(Alert.AlertType.ERROR, "La session multijoueur n'existe pas");
            joinSessionError.show();
        }
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void creationSession() throws IOException, InterruptedException {
        communication.sendMessageToServer("MULTIPLAYER_CREATION_FLAG");
        ModulesPage modulesController = new ModulesPage("fxml/multiplayerCreation.fxml");
        modulesController.initialize();
    }

    /**
     * Switch to the menu's page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull Throw when the url of the next fxml page is null
     */
    public void switchToMenu() throws IOException, UrlOfTheNextPageIsNull {
        sceneController.switchTo("fxml/menu.fxml");
    }
}
