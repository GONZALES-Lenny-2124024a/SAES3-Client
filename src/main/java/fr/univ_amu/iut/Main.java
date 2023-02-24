package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.ServerCommunication;
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
    private static ServerCommunication serverCommunication;

    public Main() throws IOException {
        serverCommunication = new ServerCommunication("127.0.0.1",10013);
    }
    /**
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @param stage the primary stage for this application, onto which
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login.fxml"));

        // The background music
        Media sound = new Media(Objects.requireNonNull(getClass().getResource("videos/music.mp3")).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

        // Screen settings
        stage.setResizable(false);  // The window isn't resizable

        stage.setTitle("Network Stories");

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("img/logo.png")).toExternalForm()));

        // The user close the application
        stage.setOnCloseRequest(event -> {
            try {
                serverCommunication.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Initialize the scene
        SceneController.setStage(stage);    // Stores the current stage
        SceneController sceneController = new SceneController();
        sceneController.initializeScene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.show();
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
    public static ServerCommunication getServerCommunication() {
        return serverCommunication;
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