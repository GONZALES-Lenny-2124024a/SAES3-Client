package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.server.ServerCommunication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private ScrollPane summaryScrollPane;
    private VBox summaryVBox;
    private ServerCommunication serverCommunication;
    private HashMap<String, Boolean> summary;

    public SummaryPage(HashMap<String, Boolean> summary) {
        this.summary = summary;
        serverCommunication = Main.getServerCommunication();
        vboxParent = new VBox();
        summaryScrollPane = new ScrollPane();
        summaryVBox = new VBox();
    }

    /**
     * Initialize the parent container
     */
    public void initializeVBoxParent() {
        vboxParent.setSpacing(15);
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
        labelUserPoint.setStyle("-fx-text-fill: WHITE; -fx-font-size: 25");

        Label labelPoint = new Label(getUserPointsFromTheServer());
        labelPoint.setStyle("-fx-text-fill: WHITE; -fx-font-size: 35");

        vboxParent.getChildren().addAll(labelUserPoint, labelPoint);
    }

    /**
     * Initialize a checkbox
     * @param answersStatus the questions and if the user answered well
     * @return the checkbox initialized
     */
    public Label initializeSumaryEntry(Map.Entry<String, Boolean> answersStatus) {
        Label label = new Label(answersStatus.getKey());
        // Style
        label.setMinWidth(500);
        label.setPadding(new Insets(30, 50, 20, 50));
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 17");
        if(answersStatus.getValue()) {
            label.setTextFill(Color.GREEN);
        } else {
            label.setTextFill(Color.RED);
        }
        return label;
    }

    /**
     * Initialize the summary entries
     */
    public void initializeSummaryEntries() {
        Iterator iteratorSummary = summary.entrySet().iterator();
        while(iteratorSummary.hasNext()) {
            summaryVBox.getChildren().add(initializeSumaryEntry((Map.Entry) iteratorSummary.next()));
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
     * Initialize the summary scroll pane
     */
    public void initializeScrollPane() {
        summaryScrollPane.setContent(summaryVBox);  // Get the content of the VBox and put it into the wcrollPane
        summaryScrollPane.setPrefViewportHeight(500);
        summaryScrollPane.setMaxWidth(750);
        summaryScrollPane.setFitToWidth(true);
        summaryScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        summaryScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        vboxParent.getChildren().add(summaryScrollPane); // Add the scroll pane to the vBox parent
    }

    /**
     * Initialize the checkboxes
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initialize() throws IOException, ClassNotFoundException, NotAStringException {
        initializeVBoxParent();
        initializeLabelsUserPoints();
        initializeScrollPane();
        initializeSummary();
        initializeButtonToLeave();
        changeScene();
    }
}
