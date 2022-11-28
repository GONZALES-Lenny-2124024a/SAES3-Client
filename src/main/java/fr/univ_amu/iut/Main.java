package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private final double HEIGHT = Screen.getPrimary().getBounds().getHeight() / 1.2;
    private final double WIDTH = Screen.getPrimary().getBounds().getWidth() / 1.2;
    private static Client client;
    public Main() throws IOException {
        client = new Client("127.0.0.1",10013);
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Screen settings
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setMaximized(true);
        stage.minHeightProperty().set(HEIGHT);
        stage.minWidthProperty().set(WIDTH);
        stage.setResizable(false);


        stage.setTitle("Network Stories");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Client getClient() {
        return client;
    }
}