package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.exceptions.NotAStringException;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.templates.CheckBoxAnswer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

/**
 * Controller of the question's page
 * @author LennyGonzales
 */
public class QuestionController implements DefaultController{
    @FXML
    private VBox vboxParent;
    @FXML
    private Label descriptionQuestion;
    @FXML
    private CheckBoxAnswer answer1;
    @FXML
    private CheckBoxAnswer answer2;
    @FXML
    private CheckBoxAnswer answer3;
    @FXML
    private TextField writtenResponseTextField;
    @FXML
    private ImageView characterImage;

    private final Communication communication;

    @FXML
    private Label timerLabel;
    private Timeline timerFunction;
    private int endTimer;
    private int timerValue;
    private Question currentQuestion;
    private static List<Question> story;
    private Iterator<Question> iteratorStory;

    public QuestionController() {
        communication = Main.getCommunication();
        endTimer = 6;   // Timer : 5 seconds
        story = new ArrayList<>();
    }

    public static List<Question> getStory() {
        return story;
    }

    /**
     * Remove answers type
     */
    public void removeAnswersType() {
        if(vboxParent.getChildren().size() <= 3) {  // If the response is a written response
            vboxParent.getChildren().remove(writtenResponseTextField); // Remove the TextField
        } else {    // If it's a QCM
            vboxParent.getChildren().remove(1,4);   // Remove all the checkboxes
        }
    }

    /**
     * Initialize the timer
     * @param timer
     */
    public void initializeTimer(int timer) {
        timerValue = timer;
        timerFunction = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    --timerValue;
                    timerLabel.setText(String.valueOf(timerValue));
                    if(timerValue <= 0) {
                        try {
                            submitAnswer();
                        } catch (IOException | NotAStringException | ClassNotFoundException | InterruptedException | UrlOfTheNextPageIsNull ex) {
                            throw new RuntimeException(ex);
                        }
                        removeAnswersType();
                    }
                })
        );
        timerFunction.setCycleCount(Timeline.INDEFINITE);
        timerFunction.play();
    }

    /**
     * Initialize the question and the answers
     * @param question question object
     */
    public void initializeVariables(Question question) {
        descriptionQuestion.setText(question.getDescription() + '\n' + question.getQuestion());
        if(question instanceof MultipleChoiceQuestion) {
            createCheckBoxes((MultipleChoiceQuestion) question);
        } else if (question instanceof WrittenResponseQuestion) {
            createWrittenResponse();
        }
    }

    /**
     * Create the checkboxes by adding them in the fxml, fix their fx:id and their font
     */
    public void createCheckBoxes(MultipleChoiceQuestion question) {
        answer1 = new CheckBoxAnswer("answer1", question.getAnswer1());
        answer2 = new CheckBoxAnswer("answer2", question.getAnswer2());
        answer3 = new CheckBoxAnswer("answer3", question.getAnswer3());
        vboxParent.getChildren().addAll(answer1,answer2,answer3);
    }

    /**
     * Create the TextField by adding it in the fxml and set his font
     */
    public void createWrittenResponse() {
        TextField textField = new TextField();
        textField.setPrefWidth(1000);
        textField.setId("writtenAnswer");
        vboxParent.getChildren().add(1, textField);
        writtenResponseTextField = textField;
    }

    /**
     * Submit the question to the server
     */
    public void submitAnswer() throws IOException, NotAStringException, ClassNotFoundException, InterruptedException, UrlOfTheNextPageIsNull {
        timerFunction.stop();

        if(vboxParent.getChildren().size() <= 3) {  // If the answer is a written response
            ((WrittenResponseQuestion) currentQuestion).setTrueAnswer(writtenResponseTextField.getText());
        } else {    // If it's a QCM
            if (answer1.isSelected()) {
                ((MultipleChoiceQuestion) currentQuestion).setTrueAnswer(1);
            } else if (answer2.isSelected()) {
                ((MultipleChoiceQuestion) currentQuestion).setTrueAnswer(2);
            } else if (answer3.isSelected()) {
                ((MultipleChoiceQuestion) currentQuestion).setTrueAnswer(3);
            }
        }

        removeAnswersType();
        nextQuestion();
    }

    /**
     * Initialize the character image who represents the module
     */
    public void initializeCharacterImage() {
        String module = story.get(0).getModule();
        String imageName = module.replace(" ", "_");    //replace space by '_'
        String urlCharacterImage = Objects.requireNonNull(getClass().getResource("img/characters/" + imageName + ".png")).toExternalForm();
        characterImage.setImage(new Image(urlCharacterImage));
    }

    @Override
    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch (message.getFlag()) {
                    case STORY -> Platform.runLater(() -> {
                        try {
                            story = ((List<Question>) message.getContent());
                            Collections.shuffle(story);     // Permutes randomly

                            initializeCharacterImage();

                            iteratorStory = story.iterator();
                            nextQuestion();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    default -> throw new NotTheExpectedFlagException("STORY");
                }
            }
        };
        communication.setMessageListener(messageListener);
    }

    public void nextQuestion() throws IOException, UrlOfTheNextPageIsNull {
        if(iteratorStory.hasNext()) {   // next question
            currentQuestion = iteratorStory.next();
            initializeVariables(currentQuestion);
            initializeTimer(endTimer);
        } else {    // Change page
            SceneController sceneController = new SceneController();
            sceneController.switchTo("fxml/summary.fxml");
        }
    }

    /**
     * Initialize the first question
     * @throws IOException if the communication with the server is closed or didn't go well
     * @throws NotTheExpectedFlagException Throw when the flag received isn't the expected flag | Print the expected flag
     * @throws ClassNotFoundException Throw if the object class not found when we receive an object from the server
     * @throws NotAStringException Throw when the message received from the server isn't a string
     */
    @FXML
    public void initialize() throws IOException, NotTheExpectedFlagException, ClassNotFoundException, NotAStringException, InterruptedException {
        initializeInteractionServer();
    }

}
