package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.gui.Speech;
import javafx.scene.control.Button;

public class ButtonSpeech extends Button {
    private Speech speech;
    public ButtonSpeech() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(speech == null) { speech = new Speech(); }
                speech.speech(getText());
            }
        });
    }
}
