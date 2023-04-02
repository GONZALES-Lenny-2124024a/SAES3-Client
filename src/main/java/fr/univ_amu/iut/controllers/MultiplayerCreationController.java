package fr.univ_amu.iut.controllers;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.gui.Speech;
import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;

/**
 * Controller of the multiplayer session's creation page
 * @author LennyGonzales
 */
public class MultiplayerCreationController implements CommunicationController {
    private static final String DEFAULT_SPEECH = "Page de création de session multijoueur";
    @FXML
    private Label codeSession;
    @FXML
    private ListView usersPresentListView;
    private final Communication communication;
    private final SceneController sceneController;
    private Speech speech;


    public MultiplayerCreationController() {
        communication = Main.getCommunication();
        sceneController = new SceneController();
        speech = new Speech();
    }

    /**
     * Initialize the interaction with the server to receive server message(s)
     */
    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch(message.getFlag()) {
                    case CODE -> Platform.runLater(() -> {  // The server sent a random code for the session
                        codeSession.setText(message.getContent().toString());
                    });
                    case NEW_PLAYER -> Platform.runLater(() -> {
                        usersPresentListView.getItems().add(message.getContent().toString());
                    });
                    default -> throw new NotTheExpectedFlagException("CODE or NEW_PLAYER");
                }
            }
        };
        communication.setMessageListener(messageListener);
    }

    /**
     * Start the session
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull Throw if the url of the next page is null
     */
    public void sessionBegin() throws IOException, UrlOfTheNextPageIsNull {
        communication.setMessageListener(null);
        communication.sendMessage(new CommunicationFormat(Flags.BEGIN));    // Send to the server that we want to start the game by clicking on the 'Start' button
        sceneController.switchTo("fxml/question.fxml");
    }

    /**
     * Cancel the session
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws UrlOfTheNextPageIsNull if the url doesn't exist
     */
    public void cancelSession() throws IOException, UrlOfTheNextPageIsNull {
        communication.sendMessage(new CommunicationFormat(Flags.CANCEL_SESSION, codeSession.getText()));
        sceneController.switchTo("fxml/menu.fxml");
    }

    /**
     * Initialize keys bind for blind people
     */
    public void initializeKeysBind() {
        codeSession.getParent().setOnKeyPressed(e -> {
            try {
                switch(e.getCode()) {
                    case DIGIT1 -> sessionBegin();
                    case DIGIT3 -> cancelSession();
                }
            } catch (IOException | UrlOfTheNextPageIsNull ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Initialize the page
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws NotTheExpectedFlagException Throw when the flag received isn't the expected flag | Print the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    @FXML
    public void initialize() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        initializeInteractionServer();

        initializeKeysBind();
        speech.initializeTextToSpeech(codeSession.getParent(), DEFAULT_SPEECH);
    }
}
