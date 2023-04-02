package fr.univ_amu.iut.fxml;

import fr.univ_amu.iut.controllers.CaptchaController;
import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.controllers.SceneController;
import fr.univ_amu.iut.gui.Speech;
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
public class TestModulesPage {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestModulesPage.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        Speech.setIsBlind(false);
                        new Main().start(TestModulesPage.this.stage);
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
        goToTheModulesPage(robot);

        assertEquals(stage.isShowing(), true);
        assertEquals(stage.getTitle(), "Network Stories");
        assertEquals(720.0, stage.getScene().getHeight());
        assertEquals(  1280.0, stage.getScene().getWidth());
    }

    @Test
    public void shouldContainsQuitButton(FxRobot robot) throws InterruptedException {
        goToTheModulesPage(robot);

        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#quit") != null);
        verifyThat("#quit", hasText("QUITTER"));
    }

    public void goToTheModulesPage(FxRobot robot) throws InterruptedException {
        TestLoginPage.connectionLoginPage(robot);
        Thread.sleep(200);
        robot.clickOn("#training");
        Thread.sleep(200);
    }
}
