package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.Communication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javafx.scene.media.Media;

import java.io.IOException;
import java.util.Objects;

/**
 * Launch the application
 * @author LennyGonzales
 */
public class Main extends Application {

    private static final double WINDOW_HEIGHT = 720.0;
    private static final double WINDOW_WIDTH = 1280.0;
    private static Communication communication;
    private static Stage stage;

    /**
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @param stage the primary stage for this application, onto which
     * @throws IOException if an error occurs during fxml loading
     */
    @Override
    public void start(Stage stage) throws IOException {
        //initializeMusic("videos/music.mp3");

        this.stage = stage;
        initializeWindow();
    }

    /**
     * Initialize the window (settings and scene)
     * @throws IOException if an error occurs during fxml loading
     */
    public void initializeWindow() throws IOException {
        // Screen settings
        stage.setResizable(false);  // The window isn't resizable
        stage.setTitle("Network Stories");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("img/logo.png")).toExternalForm()));

        // Initialize the scene
        SceneController.setStage(stage);    // Stores the current stage
        SceneController sceneController = new SceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/captcha.fxml"));
        sceneController.initializeScene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.show();
    }

    /**
     * Initialize the background music
     */
    public void initializeMusic(String fileName) {
        Media sound = new Media(Objects.requireNonNull(getClass().getResource(fileName)).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    /**
     * Launch the application
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Get the communication with the server
     * @return the communication with the server
     */
    public static Communication getCommunication() {
        return communication;
    }

    /**
     * Set the communication instance
     * @param communication the communication with the server
     */
    public static void setCommunication(Communication communication) {
        Main.communication = communication;

        stage.setOnCloseRequest(event -> {  // If the user close the application
            try {
                communication.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Get the window height reference
     * @return the height of the window
     */
    public static double getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    /**
     * Get the window width reference
     * @return the width of the window
     */
    public static double getWindowWidth() {
        return WINDOW_WIDTH;
    }
}