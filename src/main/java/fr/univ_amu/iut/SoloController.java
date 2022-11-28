package fr.univ_amu.iut;

import fr.univ_amu.iut.client.Client;
import javafx.fxml.FXML;

import java.io.IOException;

public class SoloController extends QuestionController{
    private Client client;

    public SoloController() {
        client = Main.getClient();
    }

    /**
     * Send the solo flag + Initialize the page (Prepare question and answers)
     */
    @FXML
    public void initialize() throws IOException, InterruptedException {
        client.sendMessageToServer("SOLO_FLAG");
        initializeVariables(client.receiveMessageFromServer());
    }
}
