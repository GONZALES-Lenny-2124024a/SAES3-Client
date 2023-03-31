package fr.univ_amu.iut.controllers;

import java.io.IOException;

/**
 * Interface to implements when the page need to receive message(s) from the server
 * @author LennyGonzales
 */
public interface CommunicationController {
    void initializeInteractionServer() throws IOException;
}
