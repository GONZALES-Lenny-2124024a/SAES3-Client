package fr.univ_amu.iut.controllers;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.gui.Speech;
import fr.univ_amu.iut.communication.CommunicationFormat;
import fr.univ_amu.iut.communication.Flags;
import fr.univ_amu.iut.communication.MessageListener;
import fr.univ_amu.iut.domain.MultipleChoiceQuestion;
import fr.univ_amu.iut.domain.Question;
import fr.univ_amu.iut.domain.WrittenResponseQuestion;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import fr.univ_amu.iut.templates.CheckBoxAnswer;
import fr.univ_amu.iut.templates.TextFieldSpeech;
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
public class QuestionController implements CommunicationController {
    private static final String DEFAULT_SPEECH = "Page question";
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
    private static final int NUMBER_OF_SECONDS_TIMER = 90;   // The number of seconds for the timer
    private int timerCurrentValue;
    private Question currentQuestion;
    private static List<Question> story = null;
    private Iterator<Question> iteratorStory;
    private Speech speech;

    public QuestionController() {
        communication = Main.getCommunication();
        speech = new Speech();
    }

    public static List<Question> getStory() {
        return story;
    }

    /**
     * Set the story (in the loading page) | Allows us to divide the number of communications with the server by 2 for each user in the multiplayer session (4 communications => 2 communications)
     * @param newStory the new story
     */
    public static void setStory(List<Question> newStory) { story = newStory; }

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
     * @param timer the number of seconds of the timer
     */
    public void initializeTimer(int timer) {
        timerCurrentValue = timer;
        timerFunction = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    --timerCurrentValue;
                    timerLabel.setText(String.valueOf(timerCurrentValue));
                    if(timerCurrentValue <= 0) {
                        try {
                            submitAnswer();
                        } catch (IOException | UrlOfTheNextPageIsNull ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                })
        );
        timerFunction.setCycleCount(Timeline.INDEFINITE);
        timerFunction.play();
    }

    /**
     * Initialize the question and the answers
     * @param question the current question
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
     * Create the checkboxes
     * @param question the current multiple choice question
     */
    public void createCheckBoxes(MultipleChoiceQuestion question) {
        answer1 = new CheckBoxAnswer("answer1", question.getAnswer1());
        answer2 = new CheckBoxAnswer("answer2", question.getAnswer2());
        answer3 = new CheckBoxAnswer("answer3", question.getAnswer3());
        vboxParent.getChildren().addAll(answer1,answer2,answer3);
    }

    /**
     * Create the TextField for a written response question
     */
    public void createWrittenResponse() {
        TextFieldSpeech textField = new TextFieldSpeech();
        textField.setPromptText("Entrez votre réponse écrite");
        textField.setPrefWidth(1000);
        textField.setId("writtenAnswer");
        vboxParent.getChildren().add(1, textField);
        writtenResponseTextField = textField;
    }

    /**
     * Submit the question to the server
     * @throws IOException if the switch page didn't work
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void submitAnswer() throws IOException, UrlOfTheNextPageIsNull {
        timerFunction.stop();

        if(currentQuestion instanceof WrittenResponseQuestion) {
            ((WrittenResponseQuestion) currentQuestion).setTrueAnswer(writtenResponseTextField.getText());
        } else if (currentQuestion instanceof MultipleChoiceQuestion) {
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
        String urlCharacterImage = Objects.requireNonNull(Main.class.getResource("img/characters/" + imageName + ".png")).toExternalForm();
        characterImage.setImage(new Image(urlCharacterImage));
    }

    /**
     * Initialize the interaction with the server to receive server message(s)
     */
    @Override
    public void initializeInteractionServer() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessageReceived(CommunicationFormat message) throws NotTheExpectedFlagException {
                switch (message.getFlag()) {
                    case STORY -> Platform.runLater(() -> { // If the session isn't a multiplayer session, the server sends the story here and not in the loading page
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

    /**
     * Initialize the next question
     * @throws IOException if the switch page didn't work
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void nextQuestion() throws IOException, UrlOfTheNextPageIsNull {
        if(iteratorStory.hasNext()) {   // next question
            currentQuestion = iteratorStory.next();
            initializeVariables(currentQuestion);
            initializeTimer(NUMBER_OF_SECONDS_TIMER);
            Platform.runLater(() -> descriptionQuestion.requestFocus()); // Give the focus to the description and the question after answer a question
        } else {    // Change page
            SceneController sceneController = new SceneController();
            sceneController.switchTo("fxml/summary.fxml");
        }
    }

    /**
     * Leave the session
     * @throws IOException if the communication with the server didn't go well
     * @throws UrlOfTheNextPageIsNull if the url of the next page is null
     */
    public void leave() throws IOException, UrlOfTheNextPageIsNull {
        story = null;
        communication.sendMessage(new CommunicationFormat(Flags.LEAVE_SESSION));
        SceneController sceneController = new SceneController();
        sceneController.switchTo("fxml/menu.fxml");
    }

    @FXML
    public void initialize() throws IOException, UrlOfTheNextPageIsNull {
        speech.initializeTextToSpeech(vboxParent.getParent(), DEFAULT_SPEECH);

        if(story != null) { // If it's a multiplayer session
            Collections.shuffle(story);     // Permutes randomly
            initializeCharacterImage();
            iteratorStory = story.iterator();
            nextQuestion();
        } else {
            initializeInteractionServer();
        }
    }
}