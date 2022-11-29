package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MenuController{
    private Client client;

    public MenuController() {
        client = Main.getClient();
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     */
    public void soloMode(ActionEvent event) throws IOException {
        client.sendMessageToServer("SOLO_FLAG");
        switchTo(event, "fxml/question.fxml");
    }

    /**
     * Initialize the page (Prepare question and answers)
     * @param event
     * @throws IOException
     */
    public void multiplayerMode(ActionEvent event) throws IOException {
        switchTo(event, "fxml/multiplayer.fxml");
    }

    /**
     * Switch to a next page
     * @param event
     * @param nameNextPage
     * @throws IOException
     */
    public void switchTo(ActionEvent event, String nameNextPage) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, nameNextPage);
    }
}
