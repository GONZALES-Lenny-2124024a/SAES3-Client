package fr.univ_amu.iut.exceptions;

/**
 * Throw when the url of the next fxml page is null
 * @author LennyGonzales
 */
public class UrlOfTheNextPageIsNull extends Exception {
    public UrlOfTheNextPageIsNull() {
        super("The url of the next page is null");
    }
}
