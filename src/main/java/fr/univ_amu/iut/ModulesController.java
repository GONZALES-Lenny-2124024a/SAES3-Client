package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import fr.univ_amu.iut.templates.ButtonModule;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controller of the modules page where the user choose a module to train
 */
public class ModulesController {
    private VBox vboxParent;

    private List<String> modules;
    private final Client client;
    private SceneController sceneController;
    private Button button;
    private String pageToSwitchTo;

    public ModulesController(String pageToSwitchTo) {
        modules = new ArrayList<>();
        client = Main.getClient();
        sceneController = new SceneController();
        this.pageToSwitchTo = pageToSwitchTo;
        vboxParent = new VBox();
    }

    /**
     * Method that get the modules from the server
     * @throws IOException
     * @throws ClassNotFoundException if the object isn't found (the class exists on the server but not in the client)
     */
    public void getModulesFromServer() throws IOException, ClassNotFoundException {
        modules = (List<String>) client.receiveObjectFromServer();
    }

    /**
     * Initialize the modules buttons
     */
    public void initializeModuleButtons(String pageToSwitchTo) {
        Iterator<String> iterator = modules.iterator();
        while(iterator.hasNext()) {
            button = new ButtonModule(iterator.next(), pageToSwitchTo);
            vboxParent.getChildren().add(button);
        }
    }

    /**
     * Initialize the modulesController
     * @throws IOException
     * @throws ClassNotFoundException getModulesFromServer() => if the object isn't found (the class exists on the server but not in the client)
     */
    public void initialize() throws IOException, ClassNotFoundException {
        getModulesFromServer();
        Scene scene = new Scene(vboxParent);
        initializeModuleButtons(pageToSwitchTo);
        (SceneController.getStage()).setScene(scene);
    }
}
