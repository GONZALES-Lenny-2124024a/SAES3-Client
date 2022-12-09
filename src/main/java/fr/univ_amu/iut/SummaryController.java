package fr.univ_amu.iut;

import javafx.event.ActionEvent;
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
    private HashMap<String, Boolean> summary;

    public SummaryController() {
        SceneController sceneController = new SceneController();
        summary = sceneController.getQuestionController().getAnswersStatus();
    }

    /**
     * Initialize a checkbox
     * @param question
     * @return
     */
    public CheckBox initializeCheckBox(Map.Entry<String, Boolean> question) {
        CheckBox checkBox = new CheckBox(question.getKey());
        if(question.getValue()) { checkBox.setSelected(true); }
        else { checkBox.setSelected(false); }
        checkBox.setDisable(true);
        return checkBox;
    }

    /**
     * Switch to the menu page
     * @throws IOException
     */
    public void switchTo() throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo("fxml/menu.fxml");
    }

    /**
     * Initialize the checkboxs
     */
    @FXML
    public void initialize() {
        Iterator iteratorSummary = summary.entrySet().iterator();
        while(iteratorSummary.hasNext()) {
            vbox.getChildren().add(initializeCheckBox((Map.Entry) iteratorSummary.next()));
        }
    }
}
