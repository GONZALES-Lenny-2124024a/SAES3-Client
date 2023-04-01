package fr.univ_amu.iut.gui;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.lexicon.LetterToSound;
import com.sun.speech.freetts.lexicon.LetterToSoundImpl;
import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.communication.Communication;
import fr.univ_amu.iut.controllers.SceneController;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;

import java.io.IOException;

/**
 * Represents the text-to-speech
 */
public class Speech {
    private static final KeyCode KEY = KeyCode.R;
    private static final float DEFAULT_RATE = 80f;


    private static boolean isBlind = true;

    private static Thread threadSpeech;

    public Speech() {
        interruptThreadRunning();   // stop the running thread
        threadSpeech = new Thread();
    }

    /**
     * Set if the user is blind or not
     * @param isBlind the new value of isBlind
     */
    public static void setIsBlind(boolean isBlind) {
        Speech.isBlind = isBlind;
    }

    /**
     * Return if the user is blind or not
     * @return if the user is blind or not
     */
    public static boolean getIsBlind() {
        return isBlind;
    }

    /**
     * Create a thread to read the text in the background (no freezing the application)
     * @param textToRead the text to read
     * @return if the speech worked or not
     */
    public boolean speech(String textToRead) {
        if(!isBlind) {
            return false;
        }

        interruptThreadRunning();

        threadSpeech = new Thread(()->{
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            VoiceManager voiceManager = VoiceManager.getInstance();
            Voice voice = voiceManager.getVoice("kevin16");
            voice.allocate();
            voice.setRate(DEFAULT_RATE);
            voice.speak(textToRead);
            voice.deallocate();
        });

        threadSpeech.start();
        return true;
    }

    /**
     * Text to speech letter by letter
     * @param textToRead the text to read
     * @return if the text-to-speech worked
     */
    public boolean speechLetterByLetter(String textToRead) {
        if(!isBlind) {
            return false;
        }

        interruptThreadRunning();

        threadSpeech = new Thread(()->{
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            VoiceManager voiceManager = VoiceManager.getInstance();
            Voice voice = voiceManager.getVoice("kevin16");
            voice.allocate();
            voice.setRate(DEFAULT_RATE);
            for (char letter : textToRead.toCharArray()) {
                voice.speak(String.valueOf(letter));
            }
            voice.deallocate();
        });

        threadSpeech.start();
        return true;
    }

    /**
     * Interrupt the thread if it's alive
     */
    public void interruptThreadRunning() {
        if((threadSpeech != null) && (threadSpeech.isAlive())) {
            threadSpeech.interrupt();
        }
    }

    /**
     * Initialize the text-to-speech :
     *          - key bind
     *          - set the close request
     *          - Read the speech once
     * @param parent the parent of the fxml page
     * @param textToRead the text to read
     */
    public void initializeTextToSpeech(Parent parent, String textToRead) {
        SceneController.getStage().setOnCloseRequest(event -> {  // If the user close the application
            try {
                if(threadSpeech.isAlive()) {
                    threadSpeech.interrupt();   // Interrupt the thread
                }

                Communication communication = Main.getCommunication();
                if(communication != null) {
                    communication.close();  // Close the communication
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        parent.setOnKeyPressed(e -> {
            if (e.getCode() == KEY) { // The user pressed the keyboard key specified
                speech(textToRead);
            }
        });

        parent.setFocusTraversable(true);   // Else, the parent gives the focus to its first child
        Platform.runLater(() -> parent.requestFocus()); // Give the focus to the parent

        speech(textToRead);
    }
}
