package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.IOException;

public class MultiplayerController {
    @FXML
    private TextField codeInput;
    private Client client;

    public MultiplayerController() {
        client = Main.getClient();
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
        // Send the multiplayer join flag
        client.sendMessageToServer("MULTIPLAYER_JOIN_FLAG");
        client.receiveMessageFromServer();
        client.sendMessageToServer(codeInput.getText());

        if(client.receiveMessageFromServer().equals("CODE_EXISTS_FLAG")) {  // Verify if the code is in the database
            client.changePort(Integer.valueOf(client.receiveMessageFromServer()));  // Change the port to communicate with the multiplayer session's server

            // No crash of the application
            if(client.receiveMessageFromServer().equals("PRESENCE_FLAG")) {   // To see if the server receive the connection request
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.seconds(1.0), e -> {

                            try {
                                if(client.isReceiveMessageFromServer()) {   // Verify if the server sent a message
                                    if (client.receiveMessageFromServer().equals("BEGIN_FLAG")) {    // When the game begin
                                        SceneController sceneController = new SceneController();
                                        sceneController.switchTo(event, "fxml/question.fxml");   // Switch to the question's page
                                    }
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
        }
    }

    /**
     * Send the multiplayer creation flag and switch page
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void creationSession(ActionEvent event) throws IOException, InterruptedException {
        client.sendMessageToServer("MULTIPLAYER_CREATION_FLAG");
        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, "fxml/multiplayerCreation.fxml");
    }

    /**
     * Switch to the menu's page
     * @param event
     * @throws IOException
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, "fxml/menu.fxml");
    }
}
