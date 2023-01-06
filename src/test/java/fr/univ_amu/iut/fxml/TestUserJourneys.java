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

    /**
     * The user train himself on a module, and he test himself with a single-player session
     * @param robot
     */
    @Test
    public void UserJourneyStudentTrainWithTrainingModeAndTestHimWithSoloMode(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#training");
        robot.clickOn("ALL");
        doStory(robot);
        robot.clickOn("#solo");
        doStory(robot);
    }


    /**
     * The user test himself creating a multiplayer session, then, he test himself with a single-player session
     * @param robot
     */
    @Test
    public void UserJourneyStudentTestByCreatingMultiplayerSessionAndTestHimWithSoloMode(FxRobot robot) {
        TestLoginPage.connectionLoginPage(robot);
        robot.clickOn("#multiplayer");
        robot.clickOn("#create");
        robot.clickOn("ALL");
        robot.clickOn("#start");
        doStory(robot);
        robot.clickOn("#solo");
        doStory(robot);
    }


    public void doStory(FxRobot robot) {
        while((SceneController.getStage().getScene().getRoot().lookup("#answer1") != null) || (SceneController.getStage().getScene().getRoot().lookup("#writtenAnswer") != null)) {
            robot.clickOn("#submit");
        }
        robot.clickOn("#leave");
    }
}
