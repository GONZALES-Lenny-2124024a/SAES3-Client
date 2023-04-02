package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.controllers.SceneController;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * Button to switch to menu
 * @author LennyGonzales
 */
public class ButtonSwitchToMenu extends Button {
    public ButtonSwitchToMenu() {
        setOnAction(event ->
                {
                    try {
                        SceneController sceneController = new SceneController();
                        sceneController.switchTo("fxml/menu.fxml");
                    } catch (UrlOfTheNextPageIsNull | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
