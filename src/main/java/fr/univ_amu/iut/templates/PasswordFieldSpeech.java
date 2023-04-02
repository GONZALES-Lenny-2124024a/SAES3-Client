package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.gui.Speech;
import javafx.scene.control.PasswordField;

public class PasswordFieldSpeech extends PasswordField {
    private Speech speech;
    public PasswordFieldSpeech() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(speech == null) { speech = new Speech(); }
                speech.speech(getPromptText());
            }
        });
    }
}
