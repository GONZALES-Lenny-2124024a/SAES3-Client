package fr.univ_amu.iut.exceptions;

/**
 * Throw when it's not a string
 * @author LennyGonzales
 */
public class NotAStringException extends Exception{
    public NotAStringException(Object object) {
        super("This isn't a String :" + object);
    }
}
