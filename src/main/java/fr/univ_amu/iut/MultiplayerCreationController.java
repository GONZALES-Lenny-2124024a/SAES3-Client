package fr.univ_amu.iut;

import fr.univ_amu.iut.client.ServerCommunication;
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
    private ListView<String> listView;
    private final ServerCommunication serverCommunication;
    private final SceneController sceneController;


    public MultiplayerCreationController() {
        serverCommunication = Main.getClient();
        sceneController = new SceneController();
    }

    /**
     * Send a message to the server to begin the multiplayer's session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sessionBegin() throws IOException, UrlOfTheNextPageIsNull {
        serverCommunication.sendMessageToServer("BEGIN");    // Send to the server that the host want to start the game by clicking on the 'Lancer' button
        if(serverCommunication.receiveMessageFromServer().equals("CAN_JOIN_FLAG")) { // The host can join the multiplayer's session
            serverCommunication.changePort(Integer.parseInt(serverCommunication.receiveMessageFromServer()));  // Connect to the multiplayer session
        }
        sceneController.switchTo("fxml/question.fxml");
    }

    public void getUsersPresent() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> {
                    try {
                        if(serverCommunication.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            listView.getItems().add(serverCommunication.receiveMessageFromServer());
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    @FXML
    public void initialize() throws IOException {
        if(serverCommunication.receiveMessageFromServer().equals("CODE_FLAG")) {
            codeSession.setText(serverCommunication.receiveMessageFromServer());
        }
        getUsersPresent();
    }
}
