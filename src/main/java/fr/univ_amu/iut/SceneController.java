package fr.univ_amu.iut;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Allows to switch to another page
 */
public class SceneController {

    private Scene scene;
    private static Stage stage;
    private Parent root;
    private static QuestionController questionController;

    /**
     * Set the Question controller
     * @param questionController
     */
    public void setQuestionController(QuestionController questionController) {
        this.questionController = questionController;
    }

    /**
     * Get the current controller
     * @return
     */
    public QuestionController getQuestionController() {
        return questionController;
    }

    /**
     * Get the stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Set the stage
     */
    public static void setStage(Stage s) {
        stage = s;
    }

    /**
     * Supports the switch page (with name)
     * @throws IOException
     */
    @FXML
    public void switchTo(String nameNextPage) throws IOException {
        root = FXMLLoader.load(getClass().getResource(nameNextPage));   // Load it

        // Windows size
        scene = new Scene(root,stage.getMinWidth(),stage.getMinHeight());
        stage.minHeightProperty().set(stage.getMinHeight());
        stage.minWidthProperty().set(stage.getMinWidth());

        stage.setScene(scene);
        stage.show();
    }
}
