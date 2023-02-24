package fr.univ_amu.iut;

import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;

import java.io.IOException;

/**
 * Controller of the menu's page
 * @author LennyGonzales
 */
public class MenuController{
    private final ServerCommunication serverCommunication;

    public MenuController() {
        serverCommunication = Main.getServerCommunication();
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void soloMode() throws IOException, UrlOfTheNextPageIsNull {
        serverCommunication.sendMessageToServer("SOLO_FLAG");
        switchTo("fxml/question.fxml");
    }

    /**
     * Switch to the multiplayer page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void multiplayerMode() throws IOException, UrlOfTheNextPageIsNull {
        switchTo("fxml/multiplayer.fxml");
    }

    /**
     * Switch to the login page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void disconnection() throws IOException, UrlOfTheNextPageIsNull {
        LoginController.setMail("");
        switchTo("fxml/login.fxml");
    }

    /**
     * Send the training flag + switch to the modules page
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void trainingMode() throws IOException, InterruptedException {
        serverCommunication.sendMessageToServer("TRAINING_FLAG");
        ModulesPage modulesController = new ModulesPage("fxml/question.fxml");
        modulesController.initialize();
    }

    /**
     * Switch to a next page
     * @param nameNextPage the page to switch to
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void switchTo(String nameNextPage) throws IOException, UrlOfTheNextPageIsNull {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(nameNextPage);
    }
}
