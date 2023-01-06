package fr.univ_amu.iut;

import fr.univ_amu.iut.client.ServerCommunication;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Controller of the loading's page
 */
public class LoadingController {

    private final ServerCommunication serverCommunication;
    private final SceneController sceneController;
    private Timeline timeline;

    public LoadingController() {
        serverCommunication = Main.getClient();
        sceneController = new SceneController();
    }

    /**
     * Verify if the server sent a message each second to know if the session begin
     * Allows the application to not wait indefinitely until the multiplayer session's host click on the 'Lancer' button
     */
    public void verifyServerEachSecond() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> {

                    try {
                        if(serverCommunication.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            beginSession();
                        }
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Begin the multiplayer's session
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws InterruptedException if the client disconnected
     */
    public void beginSession() throws IOException, InterruptedException {
        if (serverCommunication.receiveMessageFromServer().equals("BEGIN_FLAG")) {    // When the game begin
            timeline.stop();
            sceneController.switchTo("fxml/question.fxml");   // Switch to the question's page
        }
    }

    /**
     * Initialize the loading page
     */
    @FXML
    public void initialize() {
        verifyServerEachSecond();  // Verify if the server sent a message each second to know if the session begin
    }
}
