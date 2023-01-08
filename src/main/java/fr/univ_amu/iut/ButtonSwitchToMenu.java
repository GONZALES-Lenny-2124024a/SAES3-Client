package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.scene.control.Button;

import java.io.IOException;

public class ButtonSwitchToMenu extends Button {
    private SceneController sceneController;

    public ButtonSwitchToMenu() {
        initializeButton();
    }

    public void initializeButton() {
        setOnAction(event ->
                {
                    try {
                        sceneController = new SceneController();
                        sceneController.switchTo("fxml/menu.fxml");
                    } catch (UrlOfTheNextPageIsNull | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
