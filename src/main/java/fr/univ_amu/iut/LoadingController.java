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
