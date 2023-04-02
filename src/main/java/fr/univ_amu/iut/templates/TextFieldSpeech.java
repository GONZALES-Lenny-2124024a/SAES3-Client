package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.gui.Speech;
import javafx.scene.control.TextField;

public class TextFieldSpeech extends TextField {
    private Speech speech;
    public TextFieldSpeech() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(speech == null) { speech = new Speech(); }
                speech.speech(getPromptText());
            }
        });
    }
}
