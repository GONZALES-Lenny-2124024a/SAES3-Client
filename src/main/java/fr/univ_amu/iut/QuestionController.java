package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.HashMap;

public class QuestionController {
    @FXML
    private Label question;
    @FXML
    private CheckBox answer1;
    @FXML
    private CheckBox answer2;
    @FXML
    private CheckBox answer3;
    private final Client client;
    private HashMap<String, Boolean> answersStatus;
    public QuestionController() {
        client = Main.getClient();
        answersStatus = new HashMap<>();
    }

    public HashMap<String, Boolean> getAnswersStatus() {
        return answersStatus;
    }

    /**
     * Initialize the question and the answers
     * @throws IOException
     * @throws InterruptedException
     */
    public void initializeVariables(String questionText) throws IOException, InterruptedException {
        question.setText(questionText);        // We are obliged to do this for the endgame check
        answer1.setText(client.receiveMessageFromServer());
        answer2.setText(client.receiveMessageFromServer());
        answer3.setText(client.receiveMessageFromServer());
    }

    /**
     * Check if there are no more question
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void verifyEndGame(ActionEvent event) throws IOException, InterruptedException {
        String message = client.receiveMessageFromServer();
        if(message.equals("END_GAME_FLAG")) {
            endGame(event);
        } else {
            initializeVariables(message);
        }
    }

    /**
     * Check if the answer is correct or wrong and add it to the hash map
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void answerStatus(ActionEvent event) throws IOException, InterruptedException {
        if(client.receiveMessageFromServer().equals("CORRECT_ANSWER_FLAG")) {
            answersStatus.put(question.getText(), true);
        } else {
            answersStatus.put(question.getText(), false);
        }

        verifyEndGame(event);
    }

    /**
     * Uncheck all the checkbox
     */
    public void unselectCheckBox() {
        answer1.setSelected(false);
        answer2.setSelected(false);
        answer3.setSelected(false);
    }

    /**
     * Submit the question to the server
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    public void submitQuestion(ActionEvent event) throws IOException, InterruptedException {
        if(answer1.isSelected()) {
            client.sendMessageToServer("1");
        } else if(answer2.isSelected()) {
            client.sendMessageToServer("2");
        } else if (answer3.isSelected()) {
            client.sendMessageToServer("3");
        }

        unselectCheckBox();
        answerStatus(event);
    }

    /**
     * It stops the game
     * @param event
     */
    public void endGame(ActionEvent event) throws IOException {
        client.changePort(client.getPort());
        SceneController sceneController = new SceneController();
        sceneController.setCurrentController(this);
        sceneController.switchTo(event, "fxml/summary.fxml");
    }

    /**
     * Initialize the first question
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    public void initialize() throws IOException, InterruptedException {
        initializeVariables(client.receiveMessageFromServer());
    }
}
