package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;

public class MultiplayerController {
    @FXML
    private TextField codeInput;
    private Client client;
    private SceneController sceneController;
    private Timeline timeline;

    public MultiplayerController() {
        client = Main.getClient();
        sceneController = new SceneController();
    }

    /**
     * Send the multiplayer join flag
     * Change the port to communicate with the multiplayer session's server
     * Switch to the loading page
     *
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void joinSession(ActionEvent event) throws IOException, InterruptedException {
        sendJoinFlag(); // Send multiplayer session's code
        if(client.receiveMessageFromServer().equals("CODE_EXISTS_FLAG")) {  // Verify if the code is in the database
            client.changePort(Integer.valueOf(client.receiveMessageFromServer()));  // Change the port to communicate with the multiplayer session's server

            // Use to not run indefinitely the page (no crash page) until the host click on the 'Lancer' button
            if(client.receiveMessageFromServer().equals("PRESENCE_FLAG")) {   // To see if the server receive the connection request
                verifyServerEachSecond(event);  // Verify if the server sent a message each second to know if the session begin
            }
        }
    }

    /**
     * Verify if the server sent a message each second to know if the session begin
     * Allows the application to not wait indefinitely until the multiplayer session's host click on the 'Lancer' button
     * @param event
     */
    public void verifyServerEachSecond(ActionEvent event) {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1.0), e -> {

                    try {
                        if(client.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                            beginSession(event);
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
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void beginSession(ActionEvent event) throws IOException, InterruptedException {
        if (client.receiveMessageFromServer().equals("BEGIN_FLAG")) {    // When the game begin
            timeline.stop();
            sceneController.switchTo(event, "fxml/question.fxml");   // Switch to the question's page
        }
    }

    /**
     * Send multiplayer session's code
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendJoinFlag() throws IOException, InterruptedException {
        client.sendMessageToServer("MULTIPLAYER_JOIN_FLAG");
        client.receiveMessageFromServer();
        client.sendMessageToServer(codeInput.getText());
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void creationSession(ActionEvent event) throws IOException {
        client.sendMessageToServer("MULTIPLAYER_CREATION_FLAG");
        sceneController.switchTo(event, "fxml/multiplayerCreation.fxml");
    }

    /**
     * Switch to the menu's page
     * @param event
     * @throws IOException
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        sceneController.switchTo(event, "fxml/menu.fxml");
    }
}
