package fr.univ_amu.iut.exceptions;

public class NotAStringException extends Exception{
    public NotAStringException(Object object) {
        super("This isn't a String :" + object);
    }
}
