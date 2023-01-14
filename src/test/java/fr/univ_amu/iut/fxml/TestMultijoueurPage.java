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
 * Test the multiplayer's page
 */
@ExtendWith(ApplicationExtension.class)
public class TestMultijoueurPage {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestMultijoueurPage.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestMultijoueurPage.this.stage);
                        SceneController sceneController = new SceneController();
                        sceneController.switchTo("fxml/multiplayer.fxml");
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
    public void shouldWindowHeightEquals720() {
        assertEquals(720.0, stage.getScene().getHeight());
    }

    @Test
    public void shouldWindowWidthEquals1280() {
        assertEquals(  1280.0, stage.getScene().getWidth());
    }

    @Test
    public void shouldJoinButtonContainsRejoindreText() {
        verifyThat("#join", hasText("REJOINDRE"));
    }

    @Test
    public void shouldContainsButtonJoin() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#join") != null);
    }

    @Test
    public void shouldCreateButtonContainsCreerText() {
        verifyThat("#create", hasText("CRÉER"));
    }

    @Test
    public void shouldContainsButtonCreate() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#create") != null);
    }

    @Test
    public void shouldLeaveButtonContainsQuitterText() {
        verifyThat("#leave", hasText("QUITTER"));
    }

    @Test
    public void shouldContainsButtonLeave() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#leave") != null);
    }

    @Test
    public void shouldTextButtonContainsEmptyText() {
        verifyThat("#codeInput", hasText(""));
    }

    @Test
    public void shouldContainsCodeInputTextField() {
        assertTrue(SceneController.getStage().getScene().getRoot().lookup("#codeInput") != null);
    }
}
