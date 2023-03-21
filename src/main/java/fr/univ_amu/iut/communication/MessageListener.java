package fr.univ_amu.iut.communication;

import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;

/**
 * Allows to retrieve the desired messages on the desired page
 * @author LennyGonzales
 */
public interface MessageListener {
    void onMessageReceived(CommunicationFormat data) throws NotTheExpectedFlagException;
}
