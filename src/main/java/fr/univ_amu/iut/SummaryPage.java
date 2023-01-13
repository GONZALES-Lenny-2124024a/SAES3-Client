package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.server.ServerCommunication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Controller of the summary's page
 */
public class SummaryPage {

    private final VBox vboxParent;

    private ServerCommunication serverCommunication;
    private HashMap<String, Boolean> summary;

    public SummaryPage(HashMap<String, Boolean> summary) {
        this.summary = summary;
        serverCommunication = Main.getServerCommunication();
        vboxParent = new VBox();
    }

    /**
     * Initialize the parent container
     */
    public void initializeVBoxParent() {
        vboxParent.getStyleClass().add("background");
        vboxParent.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * Switch to the menu page
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void switchTo() throws IOException, UrlOfTheNextPageIsNull {
        SceneController sceneController = new SceneController();
        sceneController.switchTo("fxml/menu.fxml");
    }

    /**
     * Get the user points
     * @return the user points
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws ClassNotFoundException
     * @throws NotAStringException
     */
    public String getUserPointsFromTheServer() throws IOException, ClassNotFoundException, NotAStringException {
        serverCommunication.sendMessageToServer(LoginController.getMail());
        return serverCommunication.receiveMessageFromServer();
    }

    /**
     * Initialize the user points label
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initializeLabelsUserPoints() throws IOException, ClassNotFoundException, NotAStringException {
        Label labelUserPoint = new Label("Votre nouveau nombre de points : ");
        Label labelPoint = new Label(getUserPointsFromTheServer());
        vboxParent.getChildren().addAll(labelUserPoint, labelPoint);
        labelUserPoint.setStyle("-fx-text-fill: WHITE; -fx-font-size: 25");
        labelUserPoint.setPadding(new Insets(20, 0, 0, 0));
        labelPoint.setStyle("-fx-text-fill: WHITE; -fx-font-size: 35");
    }

    /**
     * Initialize a checkbox
     * @param answersStatus the questions and if the user answered well
     * @return the checkbox initialized
     */
    public Label initializeLabel(Map.Entry<String, Boolean> answersStatus) {
        Label label = new Label(answersStatus.getKey());
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        label.setPadding(new Insets(30, 200, 20, 200));
        label.setStyle("-fx-font-size: 17");
        if(answersStatus.getValue()) {
            label.setTextFill(Color.GREEN);
        } else {
            label.setTextFill(Color.RED);
        }
        return label;
    }

    public void initializeSummaryEntries() {
        Iterator iteratorSummary = summary.entrySet().iterator();
        while(iteratorSummary.hasNext()) {
            vboxParent.getChildren().add(initializeLabel((Map.Entry) iteratorSummary.next()));
        }
    }

    /**
     * Get the summary from the server
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void getSummaryFromServer() throws IOException {
        HashMap<?,?> receivedObject = (HashMap<?,?>) serverCommunication.receiveObjectFromServer();
        if((receivedObject != null) && (receivedObject.keySet().stream().allMatch(key -> key instanceof String))) {
            summary = (HashMap<String, Boolean>) receivedObject;
        }
    }

    /**
     * Initialize the summary
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initializeSummary() throws IOException {
        getSummaryFromServer();
        initializeSummaryEntries();
    }

    /**
     * Initialize the button to leave
     */
    public void initializeButtonToLeave() {
        Button button = new Button("QUITTER");
        button.setId("leave");
        button.getStyleClass().add("Btn");
        button.setPrefSize(195.0, 56.0);
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
        SceneController sceneController = new SceneController();
        sceneController.initializeScene(vboxParent, Main.getWindowWidth(), Main.getWindowHeight());
    }

    /**
     * Initialize the checkboxes
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initialize() throws IOException, ClassNotFoundException, NotAStringException {
        initializeVBoxParent();
        initializeLabelsUserPoints();
        initializeSummary();
        initializeButtonToLeave();
        changeScene();
    }
}
