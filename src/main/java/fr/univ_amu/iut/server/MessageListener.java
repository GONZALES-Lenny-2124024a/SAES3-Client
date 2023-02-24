package fr.univ_amu.iut.server;

import fr.univ_amu.iut.exceptions.NotTheExpectedFlagException;

public interface MessageListener {
    void onMessageReceived(Object data) throws NotTheExpectedFlagException;
}
