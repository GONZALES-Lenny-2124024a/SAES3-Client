package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.gui.Speech;
import javafx.scene.control.Slider;

public class SliderSpeech extends Slider {
    private Speech speech;
    public SliderSpeech() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(speech == null) { speech = new Speech(); }
                speech.speech("Nombre de questions souhaitées : " + getValue());
            }
        });
    }
}
