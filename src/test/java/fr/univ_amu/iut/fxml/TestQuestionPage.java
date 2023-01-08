package fr.univ_amu.iut.fxml;

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
 * Test the question's page
 */
@ExtendWith(ApplicationExtension.class)
public class TestQuestionPage {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestQuestionPage.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestQuestionPage.this.stage);
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
    public void shouldStageIsShowing(FxRobot robot) {
        goToQuestionPage(robot);
        assertEquals(stage.isShowing(), true);
    }

    @Test
    public void shouldGetTitle(FxRobot robot) {
        goToQuestionPage(robot);
        assertEquals(stage.getTitle(), "Network Stories");
    }

    @Test
    public void shouldWindowWidthEquals1280(FxRobot robot) {
        goToQuestionPage(robot);
        assertEquals(  1280.0, stage.getScene().getWidth());
    }

    @Test
    public void shouldWindowHeightEquals720(FxRobot robot) {
        goToQuestionPage(robot);
        assertEquals(720.0, stage.getScene().getHeight());
    }

    @Test
    public void shouldSubmitButtonContainsValiderText(FxRobot robot) {
        goToQuestionPage(robot);
        verifyThat("#submit", hasText("VALIDER"));
    }

    @Test
    public void shouldContainsButtonSubmit(FxRobot robot) {
        goToQuestionPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#submit") != null);
    }


    @Test
    public void shouldQuitButtonContainsQuitterText(FxRobot robot) {
        goToQuestionPage(robot);
        verifyThat("#quit", hasText("QUITTER"));
    }

    @Test
    public void shouldContainsButtonQuit(FxRobot robot) {
        goToQuestionPage(robot);
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#quit") != null);
    }

    public void goToQuestionPage(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#solo");
    }
}