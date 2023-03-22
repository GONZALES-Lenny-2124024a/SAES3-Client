package fr.univ_amu.iut.fxml;

import fr.univ_amu.iut.CaptchaController;
import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.SceneController;
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
 * Test the modules page
 */
@ExtendWith(ApplicationExtension.class)
public class TestMultiplayerCreationPage {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestMultiplayerCreationPage.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestMultiplayerCreationPage.this.stage);
                        CaptchaController.getTimeBeforeRefresh().stop();
                        SceneController sceneController = new SceneController();
                        sceneController.switchTo("fxml/login.fxml");
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
    public void shouldHaveDefaultSettings(FxRobot robot) throws InterruptedException {
        goToTheMultiplayerCreationPage(robot);

        assertEquals(stage.isShowing(), true);
        assertEquals(stage.getTitle(), "Network Stories");
        assertEquals(720.0, stage.getScene().getHeight());
        assertEquals(  1280.0, stage.getScene().getWidth());
    }

    @Test
    public void shouldContainsButtonStart(FxRobot robot) throws InterruptedException {
        goToTheMultiplayerCreationPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#start") != null);
        verifyThat("#start", hasText("LANCER"));
    }

    @Test
    public void shouldContainsButtonBack(FxRobot robot) throws InterruptedException {
        goToTheMultiplayerCreationPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#back") != null);
        verifyThat("#back", hasText("RETOUR"));
    }

    public void goToTheMultiplayerCreationPage(FxRobot robot) throws InterruptedException {
        TestLoginPage.connectionLoginPage(robot);
        Thread.sleep(100);
        robot.clickOn("#multiplayer");
        Thread.sleep(100);
        robot.clickOn("#create");
        Thread.sleep(200);
        robot.clickOn("Tous les modules");
        Thread.sleep(200);
    }
}
