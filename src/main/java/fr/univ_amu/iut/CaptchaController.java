package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * Controller of the captcha's page
 * @author LennyGonzales
 */
public class CaptchaController {
    private static final int MAX_TRY = 2;
    private static final int LENGTH_CAPTCHA = 8;
    private static final String CAPTCHA_CHAR_LIST = "abcdefghijklmnopqrstuvwxyz" +
                                                    "ABCDEFHIJKLMNOPQRSTUVWXYZ" +
                                                    "1234567890" +
                                                    "?!~,+-()[]{}";

    private static final int TIMER_BEFORE_REFRESH_IN_SECONDS = 20;


    private int remainingTry;
    @FXML
    private Label labelCaptcha;

    @FXML
    private TextField userInput;

    private Timeline timeBeforeRefresh;

    public CaptchaController() {
        remainingTry = MAX_TRY;
    }

    /**
     * Initialize the captcha font
     */
    public void initializeFont() {
        Font font = Font.loadFont(Objects.requireNonNull(getClass().getResource("fonts/fontCaptcha.otf")).toExternalForm(), 60);
        labelCaptcha.setFont(font);
    }

    /**
     * Initialize the captcha text and start the timer
     */
    public void initializeCaptcha() {
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for(int iFinalText = 0; iFinalText < LENGTH_CAPTCHA; ++iFinalText) {
            captchaText.append(CAPTCHA_CHAR_LIST.toCharArray()[random.nextInt(CAPTCHA_CHAR_LIST.length())]);
        }
        labelCaptcha.setText(captchaText.toString());
        initializeTimerBeforeRefresh();
    }

    /**
     * Initialize the timer
     */
    public void initializeTimerBeforeRefresh() {
        timeBeforeRefresh = new Timeline(
            new KeyFrame(Duration.seconds(TIMER_BEFORE_REFRESH_IN_SECONDS), e -> {
                timeBeforeRefresh.stop();   // stop the timer
                verifyRemainingTry();
            })
        );
        timeBeforeRefresh.setCycleCount(Timeline.INDEFINITE);
        timeBeforeRefresh.play();
    }

    /**
     * Verify the user try
     * @return if the user have entered the right answer
     * @throws UrlOfTheNextPageIsNull if the url of the next page (login page) is null
     * @throws IOException if the communication with the server didn't go well
     */
    public boolean verifyUserTry() throws UrlOfTheNextPageIsNull, IOException {
        timeBeforeRefresh.stop();   // stop the timer
        if(userInput.getText().equals(labelCaptcha.getText())) {
            Main.setCommunication(new Communication("127.0.0.1",10013));    // Start the communication with the server

            SceneController sceneController = new SceneController();
            sceneController.switchTo("fxml/login.fxml");    // Switch to the login page
            return true;
        }

        verifyRemainingTry();
        return false;
    }

    /**
     * Verify if the user still have remaining try before forced exit
     * @return if the user have remaining try
     */
    public boolean verifyRemainingTry() {
        --remainingTry;
        if(remainingTry > 0) {
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "Mauvaise réponse !");
            connexionError.show();
            userInput.clear();  // Clear the input
            initializeCaptcha();
            return true;
        }

        Platform.exit();
        return false;
    }

    @FXML
    public void initialize() {
        initializeFont();
        initializeCaptcha();
    }
}
