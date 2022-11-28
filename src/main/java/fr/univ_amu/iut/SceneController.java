package fr.univ_amu.iut;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Scene scene;
    private Stage stage;
    private Parent root;

    /**
     * Supports the switch page (with name)
     * @param event
     * @throws IOException
     */
    public void switchTo(ActionEvent event, String nameNextPage) throws IOException {
        root = FXMLLoader.load(getClass().getResource(nameNextPage));   // Load it

        // Windows size
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,stage.getMinWidth(),stage.getMinHeight());
        stage.minHeightProperty().set(stage.getMinHeight());
        stage.minWidthProperty().set(stage.getMinWidth());

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Supports the switch page (without name)
     * @param event
     * @throws IOException
     */
    @FXML
    public void switchToWithoutName(ActionEvent event) throws IOException {
        Node node = (Node) event.getSource() ;

        root = FXMLLoader.load(getClass().getResource((String) node.getUserData()));   // Load it

        // Windows size
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root,stage.getMinWidth(),stage.getMinHeight());
        stage.minHeightProperty().set(stage.getMinHeight());
        stage.minWidthProperty().set(stage.getMinWidth());

        stage.setScene(scene);
        stage.show();
    }
}
