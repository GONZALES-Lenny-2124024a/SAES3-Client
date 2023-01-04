package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
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
    private final Client client;
    private final SceneController sceneController;


    public MultiplayerCreationController() {
        client = Main.getClient();
        sceneController = new SceneController();
    }

    /**
     * Send a message to the server to begin the multiplayer's session
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sessionBegin() throws IOException {
        client.sendMessageToServer("BEGIN");    // Send to the server that the host want to start the game by clicking on the 'Lancer' button
        if(client.receiveMessageFromServer().equals("CAN_JOIN_FLAG")) { // The host can join the multiplayer's session
            client.changePort(Integer.parseInt(client.receiveMessageFromServer()));  // Connect to the multiplayer session
        }
        sceneController.switchTo("fxml/question.fxml");
    }

    public void getUsersPresent() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> {
                    try {
                        if(client.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            listView.getItems().add(client.receiveMessageFromServer());
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
        if(client.receiveMessageFromServer().equals("CODE_FLAG")) {
            codeSession.setText(client.receiveMessageFromServer());
        }
        getUsersPresent();
    }
}
