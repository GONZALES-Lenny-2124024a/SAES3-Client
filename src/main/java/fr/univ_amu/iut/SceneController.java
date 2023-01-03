package fr.univ_amu.iut;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Allows to switch to another page
 */
public class SceneController {

    private static Stage stage;
    private Parent root;
    private static QuestionController questionController;

    /**
     * Set the Question controller
     * @param questionController the question controller
     */
    public void setQuestionController(QuestionController questionController) {
        this.questionController = questionController;
    }

    /**
     * Get the current controller
     * @return the current question controller
     */
    public QuestionController getQuestionController() {
        return questionController;
    }

    /**
     * Get the stage
     * @return the stage of the window
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Set the stage
     * @param s the new stage
     */
    public static void setStage(Stage s) {
        stage = s;
    }

    /**
     * Supports the switch page (with name)
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    @FXML
    public void switchTo(String nameNextPage) throws IOException {
        URL url = getClass().getResource(nameNextPage);
        if(url != null) {
            root = FXMLLoader.load(url);   // Load it

            // Windows size
            Scene scene = new Scene(root, stage.getMinWidth(), stage.getMinHeight());
            stage.minHeightProperty().set(stage.getMinHeight());
            stage.minWidthProperty().set(stage.getMinWidth());

            stage.setScene(scene);
            stage.show();
        }
    }
}
