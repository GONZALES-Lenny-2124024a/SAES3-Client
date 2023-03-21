package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller of the multiplayer's page
 * @author LennyGonzales
 */
public class MultiplayerController implements CommunicationController {
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
     */
    public void joinSession() throws IOException {
        communication.sendMessage(new CommunicationFormat(Flags.MULTIPLAYER_JOIN, codeInput.getText())); // Send the multiplayer session code
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void creationSession() throws IOException, InterruptedException {
        ModulesPage modulesController = new ModulesPage("fxml/multiplayerCreation.fxml", Flags.CREATE_SESSION);
        modulesController.initialize();
    }

    /**
     * Initialize the interaction with the server to receive server message(s)
     */
    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch(message.getFlag()) {
                    case SESSION_EXISTS -> Platform.runLater(() -> {
                        try {
                            communication.setMessageListener(null);
                            sceneController.switchTo("fxml/loading.fxml");
                        } catch (IOException | UrlOfTheNextPageIsNull e) {
                            throw new RuntimeException(e);
                        }
                    });
                    case SESSION_NOT_EXISTS -> Platform.runLater(() -> {
                        Alert joinSessionError = new Alert(Alert.AlertType.ERROR, "La session multijoueur n'existe pas");
                        joinSessionError.show();
                    });
                    default -> throw new NotTheExpectedFlagException("SESSION_EXISTS or SESSION_NOT_EXISTS");
                }
            }
        };
        communication.setMessageListener(messageListener);
    }

    @FXML
    public void initialize() throws IOException {
        initializeInteractionServer();
    }
}
