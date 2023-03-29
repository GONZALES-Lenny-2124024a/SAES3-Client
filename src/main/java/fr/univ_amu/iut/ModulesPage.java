package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.communication.Communication;
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
public class ModulesPage extends Speech implements CommunicationController {
    private VBox vboxParent;

    private List<String> modules;
    private final Communication communication;
    private final SceneController sceneController;
    private final String pageToSwitchTo;
    private final Flags flag;

    public ModulesPage(String pageToSwitchTo, Flags flag) {
        modules = new ArrayList<>();
        communication = Main.getCommunication();
        sceneController = new SceneController();
        this.pageToSwitchTo = pageToSwitchTo;
        this.flag = flag;
        vboxParent = new VBox();
    }

    /**
     * Initialize the modules buttons
     */
    public void initializeModuleButtons(String pageToSwitchTo) {
        Iterator<String> iterator = modules.iterator();
        while(iterator.hasNext()) {
            Button button = new ButtonModule(iterator.next(), pageToSwitchTo, flag);
            button.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    speech(button.getText());
                }
            });
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
     * Initialize the interaction with the server to receive server message(s)
     * @throws IOException if the communication with the server didn't go well
     */
    public void initializeInteractionServer() throws IOException {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch(message.getFlag()) {
                    case MODULES -> Platform.runLater(() -> {   // The server sent all the modules
                        try {
                            modules = (List<String>) message.getContent();
                            initializeModuleButtons(pageToSwitchTo);
                            initializeTextToSpeech(vboxParent, "Appuyer sur la touche Tab pour naviguer. Appuyer sur espace pour sélectionner le module");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    default -> throw new NotTheExpectedFlagException("MODULES");
                }
            }
        };
        communication.setMessageListener(messageListener);
        communication.sendMessage(new CommunicationFormat(Flags.MODULES));
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
