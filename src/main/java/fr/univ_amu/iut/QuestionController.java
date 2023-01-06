package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.HashMap;

/**
 * Controller of the question's page
 */
public class QuestionController {
    @FXML
    private VBox vboxParent;
    @FXML
    private Label question;
    @FXML
    private Label description;
    @FXML
    private CheckBox answer1;
    @FXML
    private CheckBox answer2;
    @FXML
    private CheckBox answer3;
    @FXML
    private TextField writtenResponseTextField;
    private final Client client;
    private final HashMap<String, Boolean> answersStatus;
    private final Font font;

    public QuestionController() {
        client = Main.getClient();
        answersStatus = new HashMap<>();
        font = new Font("System Bold", 25);
    }

    /**
     * Get the questions and if the user answered well
     * @return the answers status hashmap (each question and if the user answered well)
     */
    public HashMap<String, Boolean> getAnswersStatus() {
        return answersStatus;
    }

    /**
     * Initialize the question and the answers
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initializeVariables(String answerType) throws IOException {
        question.setText(client.receiveMessageFromServer());        // We are obliged to do this for the endgame check
        description.setText(client.receiveMessageFromServer());
        if(answerType.equals("QCM_FLAG")) {
            createCheckBoxes();
            initializeTextCheckBoxes();
        } else if (answerType.equals("WRITTEN_RESPONSE_QUESTION_FLAG")) {
            createWrittenResponse();
        }
    }

    /**
     * Create the checkboxes by adding them in the fxml, fix their fx:id and their font
     */
    public void createCheckBoxes() {
        for (int numCheckBox = 1; numCheckBox < 4; ++numCheckBox) {
            CheckBox checkBoxAnswer = new CheckBox();
            checkBoxAnswer.setId("answer" + numCheckBox);
            checkBoxAnswer.setFont(font);
            checkBoxAnswer.setStyle("-fx-text-fill: #e7e7e7");
            vboxParent.getChildren().add(numCheckBox+2,checkBoxAnswer); // Puts this checkbox at the good position (after the title, description and question | before the button to submit and leave)
        }
        answer1 = ((CheckBox) vboxParent.lookup("#answer1"));
        answer2 = ((CheckBox) vboxParent.lookup("#answer2"));
        answer3 = ((CheckBox) vboxParent.lookup("#answer3"));
    }

    /**
     * Set the text of the checkboxes
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initializeTextCheckBoxes() throws IOException {
        answer1.setText(client.receiveMessageFromServer());
        answer2.setText(client.receiveMessageFromServer());
        answer3.setText(client.receiveMessageFromServer());
    }

    /**
     * Create the TextField by adding it in the fxml and set his font
     */
    public void createWrittenResponse() {
        TextField textField = new TextField();
        textField.setFont(font);
        textField.setPrefWidth(1000);
        textField.setId("writtenAnswer");
        vboxParent.getChildren().add(3, textField);
        writtenResponseTextField = textField;
    }

    /**
     * Submit the question to the server
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void submitAnswer() throws IOException {
        if(vboxParent.getChildren().size() <= 6) {  // If the response is a written response
            client.sendMessageToServer(writtenResponseTextField.getText());
            vboxParent.getChildren().remove(writtenResponseTextField); // Remove the TextField

        } else {    // If it's a QCM
            if (answer1.isSelected()) {
                client.sendMessageToServer("1");
            } else if (answer2.isSelected()) {
                client.sendMessageToServer("2");
            } else if (answer3.isSelected()) {
                client.sendMessageToServer("3");
            } else {
                client.sendMessageToServer("0");
            }
            vboxParent.getChildren().remove(3,6);   // Remove all the checkboxes
        }

        answerStatus(); // Check if the answer is correct or wrong and add it to the hash map
    }

    /**
     * Check if the answer is correct or wrong and add it to the hash map
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void answerStatus() throws IOException {
        if(client.receiveMessageFromServer().equals("CORRECT_ANSWER_FLAG")) {
            answersStatus.put(question.getText(), true);
        } else {
            answersStatus.put(question.getText(), false);
        }
        verifyEndGame();
    }


    /**
     * Check if there are no more question
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void verifyEndGame() throws IOException {
        String message = client.receiveMessageFromServer();
        if(message.equals("END_GAME_FLAG")) {
            endGame();
        } else {
            initializeVariables(message);
        }
    }

    /**
     * It stops the game
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void endGame() throws IOException {
        if(client.getSocketClient().getPort() != client.getPort())  { client.changePort(client.getPort()); } // If this is a multiplayer session, the user must log in to the main server
        SceneController sceneController = new SceneController();
        sceneController.setQuestionController(this);
        sceneController.switchTo("fxml/summary.fxml");
    }

    /**
     * Initialize the first question
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    @FXML
    public void initialize() throws IOException {
        initializeVariables(client.receiveMessageFromServer());
    }
}
