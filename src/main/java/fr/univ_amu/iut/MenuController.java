package fr.univ_amu.iut;

import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;

import java.io.IOException;

/**
 * Controller of the menu's page
 */
public class MenuController{
    private final ServerCommunication serverCommunication;

    public MenuController() {
        serverCommunication = Main.getServerCommunication();
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     */
    public void soloMode() throws IOException, UrlOfTheNextPageIsNull {
        serverCommunication.sendMessageToServer("SOLO_FLAG");
        switchTo("fxml/question.fxml");
    }

    /**
     * Switch to the multiplayer page
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void multiplayerMode() throws IOException, UrlOfTheNextPageIsNull {
        switchTo("fxml/multiplayer.fxml");
    }

    /**
     * Send the training flag + switch to the modules page
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws ClassNotFoundException Object not found when we receive an object from the server
     */
    public void trainingMode() throws IOException, ClassNotFoundException {
        serverCommunication.sendMessageToServer("TRAINING_FLAG");
        ModulesController modulesController = new ModulesController("fxml/question.fxml");
        modulesController.initialize();
    }

    /**
     * Switch to a next page
     * @param nameNextPage the page to switch to
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void switchTo(String nameNextPage) throws IOException, UrlOfTheNextPageIsNull {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(nameNextPage);
    }
}
