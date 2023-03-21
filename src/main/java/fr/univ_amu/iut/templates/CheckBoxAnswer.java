package fr.univ_amu.iut.templates;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Font;

/**
 * Represents a checkbox answer for a multiple choice questions
 * @author LennyGonzales
 */
public class CheckBoxAnswer extends CheckBox {
    public CheckBoxAnswer(String id, String text) {
        this.setId(id);
        this.setText(text);
        this.setWrapText(true);
        this.setFont(new Font("System Bold", 20));
        this.setStyle("-fx-text-fill: #e7e7e7");
    }
}
