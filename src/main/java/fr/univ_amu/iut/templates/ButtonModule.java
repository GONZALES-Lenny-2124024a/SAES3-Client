package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.SceneController;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import java.io.IOException;

/**
 * Create a button configured for the module page
 */
public class ButtonModule extends Button {
    private final String pageToSwitchTo;

    public ButtonModule(String text, String pageToSwitchTo) {
        this.pageToSwitchTo = pageToSwitchTo;
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
            } catch (IOException | UrlOfTheNextPageIsNull e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Send the module chose to the server
     * @param event of the button actioned
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void sendModuleChoseToServer(ActionEvent event) throws IOException {
        Main.getClient().sendMessageToServer(((Button) event.getSource()).getText()); // Send the module chosen
    }

    /**
     * Switch to the multiplayerCreation page
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void switchPage() throws IOException, UrlOfTheNextPageIsNull {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(pageToSwitchTo);  // Switch to the multiplayerCreation page
    }

    /**
     * Supports the choice of a module
     * @param event of the button actioned
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void moduleChoice(ActionEvent event) throws IOException, UrlOfTheNextPageIsNull {
        sendModuleChoseToServer(event);
        switchPage();
    }
}
