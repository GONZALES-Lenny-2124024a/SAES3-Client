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

/**
 * Test the multiplayer's page
 */
@ExtendWith(ApplicationExtension.class)
public class TestMultijoueur {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestMultijoueur.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestMultijoueur.this.stage);
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
        goToMultiplayerPage(robot);
        assertEquals(stage.isShowing(), true);
    }

    @Test
    public void shouldGetTitle(FxRobot robot) {
        goToMultiplayerPage(robot);
        assertEquals(stage.getTitle(), "Network Stories");
    }

    @Test
    public void shouldContainsButtonRejoindre(FxRobot robot) {
        goToMultiplayerPage(robot);
        verifyThat("#join", hasText("REJOINDRE"));
    }

    @Test
    public void shouldContainsButtonCreer(FxRobot robot) {
        goToMultiplayerPage(robot);
        verifyThat("#create", hasText("CRÉER"));
    }

    @Test
    public void shouldContainsButtonQuitter(FxRobot robot) {
        goToMultiplayerPage(robot);
        verifyThat("#leave", hasText("QUITTER"));
    }

    public void goToMultiplayerPage(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#multiplayer");
    }
}
