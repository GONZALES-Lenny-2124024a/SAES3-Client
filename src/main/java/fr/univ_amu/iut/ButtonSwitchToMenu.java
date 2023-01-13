package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.server.ServerCommunication;
import javafx.scene.control.Button;

import java.io.IOException;

public class ButtonSwitchToMenu extends Button {
    public ButtonSwitchToMenu() {
        initializeButton();
    }

    public void initializeButton() {
        getStyleClass().add("Btn");
        setOnAction(event ->
                {
                    try {
                        Main.getServerCommunication().sendMessageToServer("BACK_TO_MENU_FLAG");

                        SceneController sceneController = new SceneController();
                        sceneController.switchTo("fxml/menu.fxml");
                    } catch (UrlOfTheNextPageIsNull | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
