package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

/**
 * Controller of the summary's page
 * @author LennyGonzales , MathieuSauva
 */
public class SummaryController implements DefaultController {

    private Communication communication;
    private List<Question> story;

    @FXML
    private Label labelUserPoints;
    @FXML
    private ListView listViewSummary;

    public SummaryController() {    // Get story
        this.story = QuestionController.getStory(); // Get the story with the user answers
        communication = Main.getCommunication();
    }

    public void displaySummary(HashMap<Question, Boolean> summaryToDisplay) {
        Iterator iteratorSummary = summaryToDisplay.entrySet().iterator();
        Map.Entry<Question, Boolean> answerEntry;
        Label answerLabel;

        while(iteratorSummary.hasNext()) {
            answerEntry = (Map.Entry) iteratorSummary.next();

            answerLabel = new Label(answerEntry.getKey().getQuestion());
            answerLabel.setTextFill((answerEntry.getValue()) ? Color.GREEN : Color.RED);
            listViewSummary.getItems().add(answerLabel);
        }
    }

    @FXML
    public void switchToMenu() throws UrlOfTheNextPageIsNull, IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo("fxml/menu.fxml");
    }

    @Override
    public void initializeInteractionServer() throws IOException {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch (message.getFlag()) {
                    case SUMMARY -> Platform.runLater(() -> {
                        displaySummary((HashMap<Question, Boolean>) message.getContent());
                    });
                    case USER_POINTS -> Platform.runLater(() -> {
                        labelUserPoints.setText(message.getContent().toString());
                    });
                    default -> throw new NotTheExpectedFlagException("SUMMARY or USER_POINTS");
                }
            }
        };
        communication.setMessageListener(messageListener);
        communication.sendMessage(new CommunicationFormat(Flags.SUMMARY, story));   // Give the answers
    }

    /**
     * Initialize the page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    @FXML
    public void initialize() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException, InterruptedException {
        initializeInteractionServer();
    }
}
