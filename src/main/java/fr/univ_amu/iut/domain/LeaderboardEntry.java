package fr.univ_amu.iut.domain;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A leaderboard entry
 * @author LennyGonzales
 */
public class LeaderboardEntry {

    public StringProperty email;
    public IntegerProperty correctAnswers;

    public LeaderboardEntry(String email, int correctAnswers){
        this.email = new SimpleStringProperty(email);
        this.correctAnswers = new SimpleIntegerProperty(correctAnswers);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getCorrectAnswers() {
        return correctAnswers.get();
    }

    public IntegerProperty correctAnswersProperty() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers.set(correctAnswers);
    }
}
