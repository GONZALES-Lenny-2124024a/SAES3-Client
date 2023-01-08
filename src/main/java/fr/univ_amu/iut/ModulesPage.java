package fr.univ_amu.iut;

import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.templates.ButtonModule;
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
public class ModulesPage {
    private final VBox vboxParent;

    private List<String> modules;
    private final ServerCommunication serverCommunication;
    private final SceneController sceneController;
    private Button button;
    private final String pageToSwitchTo;

    public ModulesPage(String pageToSwitchTo) {
        modules = new ArrayList<>();
        serverCommunication = Main.getServerCommunication();
        sceneController = new SceneController();
        this.pageToSwitchTo = pageToSwitchTo;
        vboxParent = new VBox();
    }

    /**
     * Method that get the modules from the server
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws ClassNotFoundException if the object isn't found (the class exists on the server but not in the client)
     */
    public void getModulesFromServer() throws IOException, ClassNotFoundException {
        List<?>  receivedObject = (List<?>) serverCommunication.receiveObjectFromServer();
        if ((receivedObject != null) && (receivedObject.get(0) instanceof String)) {    //Check the cast
            modules = (List<String>) receivedObject;
        }
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
     * @throws IOException if the communication with the client is closed or didn't go well
     * @throws ClassNotFoundException getModulesFromServer() => if the object isn't found (the class exists on the server but not in the client)
     */
    public void initialize() throws IOException, ClassNotFoundException {
        getModulesFromServer();
        Scene scene = new Scene(vboxParent, Main.getWindowWidth(), Main.getWindowHeight());
        initializeModuleButtons(pageToSwitchTo);
        (SceneController.getStage()).setScene(scene);
    }
}
