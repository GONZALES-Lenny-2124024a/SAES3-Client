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
 * Test the summary page
 */
@ExtendWith(ApplicationExtension.class)
public class TestSummaryPageController {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestSummaryPageController.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        Speech.setIsBlind(false);
                        new Main().start(TestSummaryPageController.this.stage);
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
        goToTheSummaryPage(robot);

        assertEquals(stage.isShowing(), true);
        assertEquals(stage.getTitle(), "Network Stories");
        assertEquals(720.0, stage.getScene().getHeight());
        assertEquals(  1280.0, stage.getScene().getWidth());
    }

    @Test
    public void shouldContainsLeaveButton(FxRobot robot) throws InterruptedException {
        goToTheSummaryPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#leave") != null);
        verifyThat("#leave", hasText("Revenir au menu principal"));
    }

    @Test
    public void shouldContainsSummaryScrollPane(FxRobot robot) throws InterruptedException {
        goToTheSummaryPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#listViewSummary") != null);
    }

    @Test
    public void shouldContainsUserPointsLabel(FxRobot robot) throws InterruptedException {
        goToTheSummaryPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#labelUserPointsTitle") != null);
        verifyThat("#labelUserPointsTitle", hasText("Votre nouveau nombre de points : "));
    }

    @Test
    public void shouldContainsUserPoints(FxRobot robot) throws InterruptedException {
        goToTheSummaryPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#labelUserPoints") != null);
    }

    @Test
    public void shouldContainsLeaderboard(FxRobot robot) throws InterruptedException {
        goToTheSummaryPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#tableViewLeaderboard") != null);
    }

    public void goToTheSummaryPage(FxRobot robot) throws InterruptedException {
        TestLoginPage.connectionLoginPage(robot);
        Thread.sleep(100);
        robot.clickOn("#solo");
        Thread.sleep(300);
        while((SceneController.getStage().getScene().getRoot().lookup("#answer1") != null) || (SceneController.getStage().getScene().getRoot().lookup("#writtenAnswer") != null)) {
            robot.clickOn("#submit");
        }
    }
}