package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Controller of the loading's page
 * @author LennyGonzales
 */
public class LoadingController implements DefaultController{

    private final Communication communication;
    private final SceneController sceneController;


    public LoadingController() {
        communication = Main.getCommunication();
        sceneController = new SceneController();
    }

    /**
     * Cancel a session joined
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url provided is null
     */
    @FXML
    public void cancelJoin() throws IOException, UrlOfTheNextPageIsNull {
        communication.sendMessage(new CommunicationFormat(Flags.CANCEL_SESSION));
        sceneController.switchTo("fxml/multiplayer.fxml");
    }

    @Override
    public void initializeInteractionServer() throws IOException {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch(message.getFlag()) {
                    case BEGIN -> Platform.runLater(() -> {
                        try {
                            communication.setMessageListener(null);
                            communication.sendMessage(new CommunicationFormat(Flags.BEGIN, MultiplayerController.getSessionCode()));    // --!!! send sessionCode
                            sceneController.switchTo("fxml/question.fxml");   // Switch to the question's page
                        } catch (IOException | UrlOfTheNextPageIsNull e) {
                            throw new RuntimeException(e);
                        }
                    });
                    case CANCEL_SESSION -> Platform.runLater(() -> {
                        try {
                            sceneController.switchTo("fxml/multiplayer.fxml");
                            cancelJoin(); // Delete the instance of the multiplayer session in the server
                        } catch (UrlOfTheNextPageIsNull e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    default -> throw new NotTheExpectedFlagException("BEGIN");
                }
            }
        };
        communication.setMessageListener(messageListener);
    }

    /**
     * Initialize the loading page
     * Start verifying the server each second
     */
    @FXML
    public void initialize() throws NotAStringException, NotTheExpectedFlagException, IOException, UrlOfTheNextPageIsNull, ClassNotFoundException, InterruptedException {
        initializeInteractionServer();
    }
}
