package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.server.ServerCommunication;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Controller of the summary's page
 */
public class SummaryPage {

    private final VBox vboxParent;

    private ServerCommunication serverCommunication;
    private final HashMap<String, Boolean> summary;

    public SummaryPage(HashMap<String, Boolean> summary) {
        SceneController sceneController = new SceneController();
        this.summary = summary;
        serverCommunication = Main.getServerCommunication();
        vboxParent = new VBox();
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
     * Initialize the user points label
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initializeLabelUserPoints() throws IOException {
        vboxParent.getChildren().add(new Label("Votre nouveau nombre de points : " + getUserPointsFromTheServer()));
    }

    /**
     * Initialize a checkbox
     * @param answersStatus the questions and if the user answered well
     * @return the checkbox initialized
     */
    public Label initializeLabel(Map.Entry<String, Boolean> answersStatus) {
        Label label = new Label(answersStatus.getKey());
        if(answersStatus.getValue()) {
            label.setTextFill(Color.GREEN);
        } else {
            label.setTextFill(Color.RED);
        }
        return label;
    }

    /**
     * Initialize the summary
     */
    public void initializeSummary() {
        Iterator iteratorSummary = summary.entrySet().iterator();
        while(iteratorSummary.hasNext()) {
            vboxParent.getChildren().add(initializeLabel((Map.Entry) iteratorSummary.next()));
        }
    }

    /**
     * Initialize the button to leave
     */
    public void initializeButtonToLeave() {
        Button button = new Button("QUITTER");
        button.setId("leave");
        button.setOnAction(event -> {
            try {
                switchTo();
            } catch (IOException | UrlOfTheNextPageIsNull e) {
                throw new RuntimeException(e);
            }
        });
        vboxParent.getChildren().add(button);
    }

    /**
     * Change the scene to print the window
     */
    public void changeScene() {
        Scene scene = new Scene(vboxParent, Main.getWindowWidth(), Main.getWindowHeight());
        (SceneController.getStage()).setScene(scene);
    }

    /**
     * Initialize the checkboxes
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initialize() throws IOException {
        initializeLabelUserPoints();
        initializeSummary();
        initializeButtonToLeave();
        changeScene();
    }
}
