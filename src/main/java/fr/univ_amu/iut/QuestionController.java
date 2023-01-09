package fr.univ_amu.iut;

import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.server.ServerCommunication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
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
    private Label descriptionQuestion;
    @FXML
    private CheckBox answer1;
    @FXML
    private CheckBox answer2;
    @FXML
    private CheckBox answer3;
    @FXML
    private TextField writtenResponseTextField;
    private final ServerCommunication serverCommunication;
    private final HashMap<String, Boolean> summary;
    private final Font font;

    public QuestionController() {
        serverCommunication = Main.getServerCommunication();
        summary = new HashMap<>();
        font = new Font("System Bold", 25);
    }

    /**
     * Initialize the question and the answers
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initializeVariables(String answerType) throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        descriptionQuestion.setText(serverCommunication.receiveMessageFromServer() + '\n' + serverCommunication.receiveMessageFromServer());
        switch(answerType) {
            case "QCM_FLAG" :
                createCheckBoxes();
                initializeTextCheckBoxes();
                break;
            case "WRITTEN_RESPONSE_QUESTION_FLAG" :
                createWrittenResponse();
                break;
            default:
                throw new NotTheExpectedFlagException("QCM_FLAG or WRITTEN_RESPONSE_QUESTION_FLAG");
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
            vboxParent.getChildren().add(numCheckBox,checkBoxAnswer); // Puts this checkbox at the good position (after the title, description and question | before the button to submit and leave)
        }
        answer1 = ((CheckBox) vboxParent.lookup("#answer1"));
        answer2 = ((CheckBox) vboxParent.lookup("#answer2"));
        answer3 = ((CheckBox) vboxParent.lookup("#answer3"));
    }

    /**
     * Set the text of the checkboxes
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void initializeTextCheckBoxes() throws IOException, ClassNotFoundException, NotAStringException {
        answer1.setText(serverCommunication.receiveMessageFromServer());
        answer2.setText(serverCommunication.receiveMessageFromServer());
        answer3.setText(serverCommunication.receiveMessageFromServer());
    }

    /**
     * Create the TextField by adding it in the fxml and set his font
     */
    public void createWrittenResponse() {
        TextField textField = new TextField();
        textField.setFont(font);
        textField.setPrefWidth(1000);
        textField.setId("writtenAnswer");
        vboxParent.getChildren().add(1, textField);
        writtenResponseTextField = textField;
    }

    /**
     * Submit the question to the server
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void submitAnswer() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        if(vboxParent.getChildren().size() <= 3) {  // If the response is a written response
            serverCommunication.sendMessageToServer(writtenResponseTextField.getText());
            vboxParent.getChildren().remove(writtenResponseTextField); // Remove the TextField

        } else {    // If it's a QCM
            if (answer1.isSelected()) {
                serverCommunication.sendMessageToServer("1");
            } else if (answer2.isSelected()) {
                serverCommunication.sendMessageToServer("2");
            } else if (answer3.isSelected()) {
                serverCommunication.sendMessageToServer("3");
            } else {
                serverCommunication.sendMessageToServer("0");
            }
            vboxParent.getChildren().remove(1,4);   // Remove all the checkboxes
        }

        answerStatus(); // Check if the answer is correct or wrong and add it to the hash map
    }

    /**
     * Check if the answer is correct or wrong and add it to the hash map
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void answerStatus() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        switch(serverCommunication.receiveMessageFromServer()) {
            case "CORRECT_ANSWER_FLAG" -> summary.put(descriptionQuestion.getText(), true);
            case "WRONG_ANSWER_FLAG" -> summary.put(descriptionQuestion.getText(), false);
            default -> throw new NotTheExpectedFlagException("CORRECT_ANSWER_FLAG or WRONG_ANSWER_FLAG");
        }
        verifyEndGame();
    }


    /**
     * Check if there are no more question
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    public void verifyEndGame() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        String message = serverCommunication.receiveMessageFromServer();
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
    public void endGame() throws IOException, ClassNotFoundException, NotAStringException {
        if(serverCommunication.getSocketClient().getPort() != serverCommunication.getPort())  { serverCommunication.changePort(serverCommunication.getPort()); } // If this is a multiplayer session, the user must log in to the main server
        SummaryPage summaryPage = new SummaryPage(summary);
        summaryPage.initialize();
    }

    /**
     * Initialize the first question
     * @throws IOException if the communication with the client is closed or didn't go well
     */
    @FXML
    public void initialize() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException {
        initializeVariables(serverCommunication.receiveMessageFromServer());
    }
}
