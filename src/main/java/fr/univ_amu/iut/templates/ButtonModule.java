package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.SceneController;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class ButtonModule extends Button {

    public ButtonModule(String text) {
        initializeButton(text);
    }

    /**
     * Initialization of the button
     */
    public void initializeButton(String text) {
        setText(text);  // Set the button text

        // Set on action
        setOnAction(event -> {
            try {
                moduleChoice(event);    // When the player click on the button
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Send the module chose to the server
     * @param event
     * @throws IOException
     */
    public void sendModuleChoseToServer(ActionEvent event) throws IOException {
        Main.getClient().sendMessageToServer(((Button) event.getSource()).getText()); // Send the module chosen
    }

    /**
     * Switch to the multiplayerCreation page
     * @throws IOException
     */
    public void switchPage() throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo("fxml/multiplayerCreation.fxml");  // Switch to the multiplayerCreation page
    }

    /**
     * Supports the choice of a module
     * @param event
     * @throws IOException
     */
    public void moduleChoice(ActionEvent event) throws IOException {
        sendModuleChoseToServer(event);
        switchPage();
    }



}
