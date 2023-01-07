package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.server.ServerCommunication;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Controller of the summary's page
 */
public class SummaryController {
    @FXML
    private VBox vbox;
    private ServerCommunication serverCommunication;
    private final HashMap<String, Boolean> summary;

    public SummaryController() {
        SceneController sceneController = new SceneController();
        summary = sceneController.getQuestionController().getAnswersStatus();
        serverCommunication = Main.getServerCommunication();
    }

    /**
     * Initialize a checkbox
     * @param answersStatus the questions and if the user answered well
     * @return the checkbox initialized
     */
    public CheckBox initializeCheckBox(Map.Entry<String, Boolean> answersStatus) {
        CheckBox checkBox = new CheckBox(answersStatus.getKey());
        checkBox.setSelected(answersStatus.getValue());
        checkBox.setDisable(true);
        return checkBox;
    }

    /**
     * Switch to the menu page
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void switchTo() throws IOException, UrlOfTheNextPageIsNull {
        SceneController sceneController = new SceneController();
        if(serverCommunication.getPort() != 10013) { serverCommunication.changePort(10013); } // If it was a multiplayer session, the user must connect to the main server

        sceneController.switchTo("fxml/menu.fxml");
    }

    public int getUserPointsFromTheServer() throws IOException {
        serverCommunication.sendMessageToServer(LoginController.getMail());
        return Integer.parseInt(serverCommunication.receiveMessageFromServer());
    }

    /**
     * Initialize the checkboxes
     */
    @FXML
    public void initialize() throws IOException {
        vbox.getChildren().add(new Label("Votre nouveau elo : " + getUserPointsFromTheServer()));

        Iterator iteratorSummary = summary.entrySet().iterator();
        while(iteratorSummary.hasNext()) {
            vbox.getChildren().add(initializeCheckBox((Map.Entry) iteratorSummary.next()));
        }
    }
}
