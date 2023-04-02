package fr.univ_amu.iut.templates;

import fr.univ_amu.iut.gui.Speech;

public class ButtonAudioDescription extends ButtonSpeech{
    public ButtonAudioDescription(){
        setOnAction(e -> {
            Speech.setIsBlind(!Speech.getIsBlind());
        });
    }
}
