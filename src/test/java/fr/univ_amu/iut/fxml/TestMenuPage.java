package fr.univ_amu.iut.fxml;

import fr.univ_amu.iut.*;
import fr.univ_amu.iut.Speech;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

/**
 * Test the menu's page
 */
@ExtendWith(ApplicationExtension.class)
public class TestMenuPage {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestMenuPage.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        Speech.setIsBlind(false);
                        new Main().start(TestMenuPage.this.stage);
                        CaptchaController.getTimeBeforeRefresh().stop();
                        SceneController sceneController = new SceneController();
                        sceneController.switchTo("fxml/menu.fxml");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        });
    }

    @AfterEach
    void afterEachTest(FxRobot robot) throws TimeoutException {
        FxToolkit.cleanupStages();
        robot.release(new KeyCode[]{});
        robot.release(new MouseButton[]{});
    }

    @Test
    public void shouldHaveDefaultSettings() {
        assertEquals(stage.isShowing(), true);
        assertEquals(stage.getTitle(), "Network Stories");
        assertEquals(720.0, stage.getScene().getHeight());
        assertEquals(  1280.0, stage.getScene().getWidth());
    }

    @Test
    public void shouldContainsPasswordButton() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#solo") != null);
        verifyThat("#solo", hasText("SOLO"));
    }

    @Test
    public void shouldContainsMultiplayerButton() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#multiplayer") != null);
        verifyThat("#multiplayer", hasText("MULTIJOUEUR"));
    }

    @Test
    public void shouldContainsTrainingButton() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#training") != null);
        verifyThat("#training", hasText("ENTRAINEMENT"));
    }

    @Test
    public void shouldContainsDeconnexionButton() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#disconnection") != null);
        verifyThat("#disconnection", hasText("DECONNEXION"));
    }

    @Test
    public void shouldContainsSlider() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#nbQuestionsSlider") != null);
    }
}
