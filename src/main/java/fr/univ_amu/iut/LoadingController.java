package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.application.Platform;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.List;

/**
 * Controller of the loading's page
 * @author LennyGonzales
 */
public class LoadingController implements CommunicationController {

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

    /**
     * Initialize the interaction with the server to receive server message(s)
     */
    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch(message.getFlag()) {
                    case STORY -> Platform.runLater(() -> { // The multiplayer session begin
                        try {
                            communication.setMessageListener(null);
                            QuestionController.setStory((List<Question>) message.getContent());
                            sceneController.switchTo("fxml/question.fxml");   // Switch to the question's page
                        } catch (IOException | UrlOfTheNextPageIsNull e) {
                            throw new RuntimeException(e);
                        }
                    });
                    case CANCEL_SESSION -> Platform.runLater(() -> {    // If the host of the multiplayer session canceled the session
                        try {
                            cancelJoin(); // Delete the instance of the multiplayer session in the server
                        } catch (IOException | UrlOfTheNextPageIsNull e) {
                            throw new RuntimeException(e);
                        }
                    });
                    default -> throw new NotTheExpectedFlagException("BEGIN or CANCEL_SESSION");
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
