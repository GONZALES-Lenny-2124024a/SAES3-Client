package fr.univ_amu.iut;

import fr.univ_amu.iut.server.ServerCommunication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import javafx.scene.media.Media;

import java.io.File;
import java.io.IOException;

/**
 * Launch the application
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
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login.fxml"));

        Media sound = new Media(getClass().getResource("testAudio.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        // Screen settings
        stage.setResizable(false);

        stage.setTitle("Network Stories");

        SceneController sceneController = new SceneController();
        sceneController.setStage(stage);    // Stores the current stage
        sceneController.initializeScene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Get the client
     * @return the client
     */
    public static ServerCommunication getServerCommunication() {
        return serverCommunication;
    }

    public static double getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    public static double getWindowWidth() {
        return WINDOW_WIDTH;
    }
}