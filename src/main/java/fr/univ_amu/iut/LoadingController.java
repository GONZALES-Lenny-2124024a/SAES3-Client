package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Controller of the loading's page
 * @author LennyGonzales
 */
public class LoadingController {

    private final ServerCommunication serverCommunication;
    private final SceneController sceneController;
    private Timeline verifyServerEachSecondTimeLine;

    public LoadingController() {
        serverCommunication = Main.getServerCommunication();
        sceneController = new SceneController();
    }

    /**
     * Verify if the server sent a message each second to know if the multiplayer session begin
     * Allows the application to not wait indefinitely (freeze) until the multiplayer session's host click on the 'Lancer' button
     */
    public void verifyServerEachSecond() {

        verifyServerEachSecondTimeLine = new Timeline(
                new KeyFrame(Duration.seconds(0.05), e -> {

                    try {
                        if(serverCommunication.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            beginSession();
                        }
                    } catch (IOException | UrlOfTheNextPageIsNull | NotTheExpectedFlagException |
                             ClassNotFoundException | NotAStringException ex) {
                        throw new RuntimeException(ex);
                    }

                })
        );
        verifyServerEachSecondTimeLine.setCycleCount(Timeline.INDEFINITE);
        verifyServerEachSecondTimeLine.play();
    }

    /**
     * Start the multiplayer's session
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull Throw when the url of the next fxml page is null
     * @throws NotTheExpectedFlagException Throw when the flag received isn't the expected flag | Print the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    public void beginSession() throws IOException, UrlOfTheNextPageIsNull, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        verifyServerEachSecondTimeLine.stop();  // stop verifying the server each second

        if (!(serverCommunication.receiveMessageFromServer()).equals("BEGIN_FLAG")) {    // When the host start the game by clicking on the 'Start' button
            throw new NotTheExpectedFlagException("BEGIN_FLAG");
        }
        serverCommunication.sendMessageToServer("BEGIN_FLAG");  // Say to the server to start the game
        sceneController.switchTo("fxml/question.fxml");   // Switch to the question's page
    }

    /**
     * Initialize the loading page
     * Start verifying the server each second
     */
    @FXML
    public void initialize() {
        verifyServerEachSecond();  // Verify if the server sent a message each second to know if the session begin
    }
}
