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

/**
 * Test the user journeys
 */
@ExtendWith(ApplicationExtension.class)
public class TestUserJourneys {
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        Platform.runLater(() -> {
            TestUserJourneys.this.stage = new Stage();
            try {
                FxToolkit.setupStage((sta) -> {
                    try {
                        new Main().start(TestUserJourneys.this.stage);
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

    /**
     * The user isn't a registered user
     * @param robot
     */
    @Test
    public void UserJourneyNotARegisteredUser(FxRobot robot) {
        robot.clickOn("#mailTextField");
        robot.write("lg@gmail.com");
        robot.clickOn("#passwordTextField");
        robot.write("lazvbiu1324");
        robot.clickOn("#submit");
        robot.clickOn("OK");
    }
}
