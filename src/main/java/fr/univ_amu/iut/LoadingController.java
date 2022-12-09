package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Controller of the loading's page
 */
public class LoadingController {

    private Client client;
    private SceneController sceneController;
    private Timeline timeline;

    public LoadingController() {
        client = Main.getClient();
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
                        if(client.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            beginSession();
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Begin the multiplayer's session
     * @throws IOException
     * @throws InterruptedException
     */
    public void beginSession() throws IOException, InterruptedException {
        if (client.receiveMessageFromServer().equals("BEGIN_FLAG")) {    // When the game begin
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
