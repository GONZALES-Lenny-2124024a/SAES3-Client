package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.server.ServerCommunication;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    public void initialize() throws IOException, UrlOfTheNextPageIsNull {
        // User points
        vboxParent.getChildren().add(new Label("Votre nouveau nombre de points : " + getUserPointsFromTheServer()));

        // Summary
        Iterator iteratorSummary = summary.entrySet().iterator();
        while(iteratorSummary.hasNext()) {
            vboxParent.getChildren().add(initializeCheckBox((Map.Entry) iteratorSummary.next()));
        }

        // Button to leave
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

        // Print the window
        Scene scene = new Scene(vboxParent);
        (SceneController.getStage()).setScene(scene);
    }
}
