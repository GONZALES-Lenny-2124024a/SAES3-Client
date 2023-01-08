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
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

/**
 * Test the login's page
 */
@ExtendWith(ApplicationExtension.class)
public class TestLoginPage {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestLoginPage.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestLoginPage.this.stage);
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
    public void shouldStageIsShowing() {
        assertEquals(stage.isShowing(), true);
    }

    @Test
    public void shouldGetTitle() {
        assertEquals(stage.getTitle(), "Network Stories");
    }

    @Test
    public void shouldWindowHeightEquals720() { assertEquals(720.0, stage.getScene().getHeight()); }

    @Test
    public void shouldWindowWidthEquals1280() { assertEquals(  1280.0, stage.getScene().getWidth()); }

    @Test
    public void shouldSubmitButtonContainsSeConnecterText() {
        verifyThat("#submit", hasText("SE CONNECTER"));
    }

    @Test
    public void shouldContainsButtonSubmit() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#submit") != null);
    }

    @Test
    public void shouldMailTextFieldContainsEmptyText() {
        verifyThat("#mailTextField", hasText(""));
    }

    @Test
    public void shouldContainsMailTextField() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#mailTextField") != null);
    }

    @Test
    public void shouldPasswordTextFieldContainsEmptyText() {
        verifyThat("#passwordTextField", hasText(""));
    }

    @Test
    public void shouldContainsPasswordlTextField() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#passwordTextField") != null);
    }

    @Test
    public void shouldNotLogin(FxRobot robot) {
        robot.clickOn("#submit");
        robot.clickOn("OK");
    }

    @Test
    public void shouldLogin(FxRobot robot) {
        connectionLoginPage(robot);
    }

    public static void connectionLoginPage(FxRobot robot) {
        robot.clickOn("#mailTextField");
        robot.write("lenny.gonzales@etu.univ-amu.fr");
        robot.clickOn("#passwordTextField");
        robot.write("lenny");
        robot.clickOn("#submit");
    }
}