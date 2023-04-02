package fr.univ_amu.iut.controllers;

import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.gui.Speech;
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
                                                    "1234567890";

    private static final int TIMER_BEFORE_REFRESH_IN_SECONDS = 40;
    private static final String ERROR_INCORRECT_ANSWER = "Mauvaise réponse";
    private static final String DEFAULT_SPEECH = "Page Captcha, appuyez sur tab pour naviguer, appuyez sur espace pour valider";

    private int remainingTry;
    @FXML
    private Label labelCaptcha;

    @FXML
    private TextField userInput;
    private Speech speech;

    private int timerCurrentValue;
    @FXML
    private Label timerLabel;

    private static Timeline timeBeforeRefresh;
    public CaptchaController() {
        remainingTry = MAX_TRY;
        speech = new Speech();
    }

    /**
     * Get the timeline
     * @return the timeline object
     */
    public static Timeline getTimeBeforeRefresh() {
        return timeBeforeRefresh;
    }

    /**
     * Initialize the captcha font
     */
    public void initializeFont() {
        Font font = Font.loadFont(Main.class.getResourceAsStream("fonts/zxx.ttf"), 60);
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
        timerCurrentValue = TIMER_BEFORE_REFRESH_IN_SECONDS;
        timeBeforeRefresh = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    --timerCurrentValue;
                    timerLabel.setText(String.valueOf(timerCurrentValue));
                    if(timerCurrentValue <= 0) {
                        timeBeforeRefresh.stop();
                        try {
                            verifyRemainingTry();
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
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
    public boolean verifyUserTry() throws UrlOfTheNextPageIsNull, IOException, InterruptedException {
        timeBeforeRefresh.stop();   // stop the timer
        if(userInput.getText().equals(labelCaptcha.getText())) {
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
    public boolean verifyRemainingTry() throws InterruptedException {
        --remainingTry;
        if (remainingTry > 0) {
            Alert connexionError = new Alert(Alert.AlertType.ERROR, ERROR_INCORRECT_ANSWER);
            connexionError.show();
            speech.speech(ERROR_INCORRECT_ANSWER + " cliques sur la touche entrer pour pouvoir recommencer");
            userInput.clear();  // Clear the input
            initializeCaptcha();
            return true;
        }

        Platform.exit();
        return false;
    }

    /**
     * Set isBlind variable
     */
    public void setIsBlind() {
        speech.interruptThreadRunning();
        Speech.setIsBlind(!Speech.getIsBlind());
    }

    @FXML
    public void initialize() throws InterruptedException {
        initializeFont();
        initializeCaptcha();

        speech.initializeTextToSpeech(labelCaptcha.getParent(), DEFAULT_SPEECH);
    }
}