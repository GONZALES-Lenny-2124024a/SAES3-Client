package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import java.io.IOException;

/**
 * Controller of the menu's page
 */
public class MenuController{
    private final Client client;

    public MenuController() {
        client = Main.getClient();
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     */
    public void soloMode() throws IOException {
        client.sendMessageToServer("SOLO_FLAG");
        switchTo("fxml/question.fxml");
    }

    /**
     * Initialize the page (Prepare question and answers)
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void multiplayerMode() throws IOException {
        switchTo("fxml/multiplayer.fxml");
    }

    public void trainingMode() throws IOException, ClassNotFoundException {
        client.sendMessageToServer("TRAINING_FLAG");
        ModulesController modulesController = new ModulesController("fxml/question.fxml");
        modulesController.initialize();
    }

    /**
     * Switch to a next page
     * @param nameNextPage the page to switch to
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void switchTo(String nameNextPage) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(nameNextPage);
    }
}
