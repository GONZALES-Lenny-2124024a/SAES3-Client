package fr.univ_amu.iut;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
    private final HashMap<String, Boolean> summary;

    public SummaryController() {
        SceneController sceneController = new SceneController();
        summary = sceneController.getQuestionController().getAnswersStatus();
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
    public void switchTo() throws IOException {
        SceneController sceneController = new SceneController();
        Main.getClient().changePort(10013);

        sceneController.switchTo("fxml/menu.fxml");
    }

    /**
     * Initialize the checkboxes
     */
    @FXML
    public void initialize() {
        Iterator iteratorSummary = summary.entrySet().iterator();
        while(iteratorSummary.hasNext()) {
            vbox.getChildren().add(initializeCheckBox((Map.Entry) iteratorSummary.next()));
        }
    }
}
