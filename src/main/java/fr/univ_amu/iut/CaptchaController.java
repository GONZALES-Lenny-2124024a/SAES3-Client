package fr.univ_amu.iut;

import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.exceptions.UrlOfTheNextPageIsNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class CaptchaController {
    private static final int MAX_TRY = 2;
    private static final int LENGTH_CAPTCHA = 8;
    private static final String CAPTCHA_CHAR_LIST = "abcdefghijklmnopqrstuvwxyz" +
                                                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                                    "1234567890" +
                                                    "?!~,+-()[]{}";

    private int remainingTry;
    @FXML
    private Label labelCaptcha;

    @FXML
    private TextField userInput;

    public CaptchaController() {
        remainingTry = MAX_TRY;
    }

    public void initializeFont() {
        Font font = Font.loadFont(Objects.requireNonNull(getClass().getResource("fonts/fontCaptcha.otf")).toExternalForm(), 60);
        labelCaptcha.setFont(font);
    }

    public void initializeCaptcha() {
        Random random = new Random();
        StringBuilder captchaText = new StringBuilder();
        for(int iFinalText = 0; iFinalText < LENGTH_CAPTCHA; ++iFinalText) {
            captchaText.append(CAPTCHA_CHAR_LIST.toCharArray()[random.nextInt(CAPTCHA_CHAR_LIST.length())]);
        }
        labelCaptcha.setText(captchaText.toString());
    }

    public boolean verifyUserTry() throws UrlOfTheNextPageIsNull, IOException {
        if(userInput.getText().equals(labelCaptcha.getText())) {
            Main.setCommunication(new Communication("127.0.0.1",10013));

            SceneController sceneController = new SceneController();
            sceneController.switchTo("fxml/login.fxml");
            return true;
        }

        --remainingTry;
        if(remainingTry > 0) {
            Alert connexionError = new Alert(Alert.AlertType.ERROR, "Mauvaise réponse !");
            connexionError.show();
            initializeCaptcha();
            return false;
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
