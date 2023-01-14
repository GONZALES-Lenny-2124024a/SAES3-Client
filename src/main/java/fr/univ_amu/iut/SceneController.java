package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Allows to switch to another page
 * @author LennyGonzales
 */
public class SceneController {

    private static Stage stage;

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
     * Initialize the scene
     * @param parent the parent of the scene
     * @param width the width of the window
     * @param height the height of the window
     */
    public void initializeScene(Parent parent, double width, double height) {
        Scene scene = new Scene(parent, width, height);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/style.css")).toExternalForm());
        stage.setScene(scene);
    }

    /**
     * Supports the switch page
     * @param nameNextPage the name of the next page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull Throw if the url of the next page is null
     */
    @FXML
    public void switchTo(String nameNextPage) throws UrlOfTheNextPageIsNull, IOException {
        URL url = getClass().getResource(nameNextPage);
        if(url == null) {
            throw new UrlOfTheNextPageIsNull();
        }
        Parent root = FXMLLoader.load(url);   // Load it

        initializeScene(root,Main.getWindowWidth(), Main.getWindowHeight());
        stage.show();
    }
}
