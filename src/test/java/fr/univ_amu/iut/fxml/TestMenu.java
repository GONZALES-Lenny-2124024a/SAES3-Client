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

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

@ExtendWith(ApplicationExtension.class)
public class TestMenu {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestMenu.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestMenu.this.stage);
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
        assertEquals(stage.isShowing(), true);
    }

    @Test
    public void shouldGetTitle(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        assertEquals(stage.getTitle(), "Network Stories");
    }

    @Test
    public void shouldContainsButtonSolo(FxRobot robot) throws IOException, InterruptedException {
        TestLoginPage.connectionLoginPage(robot);
        verifyThat("#solo", hasText("SOLO"));

    }

    @Test
    public void shouldContainsButtonMultijoueur(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        verifyThat("#multiplayer", hasText("MULTIJOUEUR"));
    }

    @Test
    public void shouldContainsButtonEntrainement(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        verifyThat("#training", hasText("ENTRAINEMENT"));
    }

    @Test
    public void shouldContainsButtonDeconnexion(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        verifyThat("#deconnexion", hasText("DÉCONNEXION"));
    }
}
