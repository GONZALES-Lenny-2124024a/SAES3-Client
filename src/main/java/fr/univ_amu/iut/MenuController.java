package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.Slider;

import java.io.IOException;

/**
 * Controller of the menu's page
 * @author LennyGonzales
 */
public class MenuController {
    private final Communication communication;
    @FXML
    private Slider nbQuestionsSlider;
    private static IntegerProperty nbQuestions;

    public MenuController() {
        communication = Main.getCommunication();
        nbQuestions = new SimpleIntegerProperty();
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void soloMode() throws IOException, UrlOfTheNextPageIsNull {
        communication.sendMessage(new CommunicationFormat(Flags.SOLO, nbQuestions.get()));
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
        ModulesPage modulesController = new ModulesPage("fxml/question.fxml", Flags.TRAINING);
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

    public static int getNbQuestions() {
        return nbQuestions.get();
    }

    @FXML
    public void initialize() {
        nbQuestions.bindBidirectional(nbQuestionsSlider.valueProperty());
    }
}
