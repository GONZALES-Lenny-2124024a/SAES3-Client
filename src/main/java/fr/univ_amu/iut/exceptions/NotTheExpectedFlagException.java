package fr.univ_amu.iut.exceptions;

public class NotTheExpectedFlagException extends Exception{
    public NotTheExpectedFlagException(String expectedFlag) {
        super("This is not the expected flag. The expected flag is : " + expectedFlag);
    }
}