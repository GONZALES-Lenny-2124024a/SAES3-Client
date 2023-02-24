package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.communication.ServerCommunication;
import fr.univ_amu.iut.templates.ButtonModule;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.*;

/**
 * Controller of the modules page where the user choose a module to practice on
 * @author LennyGonzales
 */
public class ModulesPage {
    private VBox vboxParent;

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
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void getModulesFromServer() throws IOException, InterruptedException {
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
     * Initialize the button which return to the menu page
     */
    public void initializeSwitchToMenuButton() {
        ButtonSwitchToMenu buttonSwitchToMenu = new ButtonSwitchToMenu();
        buttonSwitchToMenu.setText("QUITTER");
        buttonSwitchToMenu.setId("quit");
        vboxParent.getChildren().add(buttonSwitchToMenu);
    }

    /**
     * Initialize the root node of the page
     */
    public void initializeVBoxParent() {
        vboxParent = new VBox();
        vboxParent.getStyleClass().add("background");  // Add a style class to get css style
        vboxParent.setAlignment(Pos.CENTER);
        vboxParent.setSpacing(20.0);    // Space between children
    }

    /**
     * Initialize the interaction with the server
     */
    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(Object message) throws NotTheExpectedFlagException {
                if (message instanceof HashMap) {

                    Iterator it = ((HashMap<Flags, List<String>>) message).entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<Flags, List<String>> entry = (Map.Entry)it.next(); // Get element

                        // Use element
                        switch(entry.getKey()) {
                            case MODULES -> Platform.runLater(() -> {
                                modules = entry.getValue();
                                initializeModuleButtons(pageToSwitchTo);
                            });
                            default -> throw new NotTheExpectedFlagException("MODULES");
                        }

                        // Remove Element
                        it.remove();
                    }
                }
            }
        };
        serverCommunication.setMessageListener(messageListener);
    }


    /**
     * Initialize the modulesController
     * @throws IOException if the communication with the server is closed or didn't go well
     */
    public void initialize() throws IOException, InterruptedException {
        initializeInteractionServer();
        initializeVBoxParent();
        initializeSwitchToMenuButton();
        sceneController.initializeScene(vboxParent, Main.getWindowWidth(), Main.getWindowHeight());
    }
}
