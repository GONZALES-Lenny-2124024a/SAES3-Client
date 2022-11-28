package fr.univ_amu.iut.fxml;

import fr.univ_amu.iut.Main;
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
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

@ExtendWith(ApplicationExtension.class)
public class TestQuestion {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestQuestion.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestQuestion.this.stage);
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
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#solo");
        assertEquals(stage.isShowing(), true);
    }

    @Test
    public void shouldGetTitle(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#solo");
        assertEquals(stage.getTitle(), "Network Stories");
    }

    @Test
    public void shouldContainsButtonValider(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#solo");
        verifyThat("#submit", hasText("VALIDER"));
    }

    @Test
    public void shouldContainsButtonDeconnexion(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#solo");
        verifyThat("#quit", hasText("QUITTER"));
    }
}