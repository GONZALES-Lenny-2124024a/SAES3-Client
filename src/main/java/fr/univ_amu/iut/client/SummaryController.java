package fr.univ_amu.iut.client;

import fr.univ_amu.iut.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SummaryController {
    @FXML
    private VBox vbox;
    private HashMap<String, Boolean> summary;

    public SummaryController() {
        SceneController sceneController = new SceneController();
        summary = sceneController.getCurrentController().getAnswersStatus();
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
     * @param event
     * @throws IOException
     */
    public void switchTo(ActionEvent event) throws IOException {
        SceneController sceneController = new SceneController();
        sceneController.switchTo(event, "fxml/menu.fxml");
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
