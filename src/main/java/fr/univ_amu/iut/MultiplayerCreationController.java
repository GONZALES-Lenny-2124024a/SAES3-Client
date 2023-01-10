package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Controller of the multiplayer session's creation page
 */
public class MultiplayerCreationController extends QuestionController{
    @FXML
    private TextField codeSession;
    @FXML
    private ListView<String> usersPresentListView;
    private final ServerCommunication serverCommunication;
    private final SceneController sceneController;


    public MultiplayerCreationController() {
        serverCommunication = Main.getServerCommunication();
        sceneController = new SceneController();
    }

    /**
     * Start the session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sessionBegin() throws IOException, UrlOfTheNextPageIsNull, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        serverCommunication.sendMessageToServer("BEGIN");    // Send to the server that we want to start the game by clicking on the 'Start' button
        if(!(serverCommunication.receiveMessageFromServer().equals("BEGIN_FLAG"))) { // The game begins
            throw new NotTheExpectedFlagException("BEGIN_FLAG");
        }
        serverCommunication.sendMessageToServer("BEGIN_FLAG");  // Send to the server that the game begin
        sceneController.switchTo("fxml/question.fxml");
    }

    /**
     * Get users who joined the multiplayer session
     */
    public void getEmailOfTheUsersWhoJoined() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> {
                    try {
                        if(serverCommunication.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            usersPresentListView.getItems().add(serverCommunication.receiveMessageFromServer());
                        }
                    } catch (IOException | ClassNotFoundException | NotAStringException ex) {
                        throw new RuntimeException(ex);
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Get the session code from the server
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void getSessionCode() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        if(!(serverCommunication.receiveMessageFromServer().equals("CODE_FLAG"))) {
            throw new NotTheExpectedFlagException("CODE_FLAG");
        }
        codeSession.setText(serverCommunication.receiveMessageFromServer());
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    @FXML
    public void initialize() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        getSessionCode();
        getEmailOfTheUsersWhoJoined();
    }
}
