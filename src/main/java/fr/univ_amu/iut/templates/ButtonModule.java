package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.SceneController;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import java.io.IOException;

/**
 * Create a configured Button for the Modules page
 * @author LennyGonzales
 */
public class ButtonModule extends Button {
    private final String pageToSwitchTo;

    /**
     * Initialize the button
     * @param text the button text
     * @param pageToSwitchTo the page to switch to when the user click on a module
     */
    public ButtonModule(String text, String pageToSwitchTo) {
        this.pageToSwitchTo = pageToSwitchTo;
        initializeButton(text);
    }

    /**
     * Initialization of the button
     */
    public void initializeButton(String text) {
        setText(text);  // Set the button text
        getStyleClass().add("Btn"); // Add a style class to get css style
        setPrefSize(160.0, 40.0);
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
     * Send the module chosen to the server
     * @param event of the button actioned
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void sendModuleChoseToServer(ActionEvent event) throws IOException {
        Main.getServerCommunication().sendMessageToServer(((Button) event.getSource()).getText()); // Send the module chosen
    }

    /**
     * Switch page
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void switchPage() throws IOException, UrlOfTheNextPageIsNull {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(pageToSwitchTo);
    }

    /**
     * Supports the user choice of a module
     * @param event of the button actioned
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void moduleChoice(ActionEvent event) throws IOException, UrlOfTheNextPageIsNull {
        sendModuleChoseToServer(event);
        switchPage();
    }
}
