package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.gui.Speech;
import javafx.scene.control.Label;

public class LabelSpeech extends Label {
    private Speech speech;

    public LabelSpeech() {
        setFocusTraversable(true);
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(speech == null) { speech = new Speech(); }
                speech.speech(getText());
            }
        });
    }
}
