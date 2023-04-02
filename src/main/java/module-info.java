module fr.univ_amu.iut {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires java.desktop;
    requires freetts;

    opens fr.univ_amu.iut to javafx.fxml;
    exports fr.univ_amu.iut;
    exports fr.univ_amu.iut.communication;
    opens fr.univ_amu.iut.communication to javafx.fxml;
    opens fr.univ_amu.iut.domain to javafx.base;
    exports fr.univ_amu.iut.templates;
    opens fr.univ_amu.iut.templates to javafx.fxml;
    exports fr.univ_amu.iut.controllers;
    opens fr.univ_amu.iut.controllers to javafx.fxml;
    exports fr.univ_amu.iut.gui;
    opens fr.univ_amu.iut.gui to javafx.fxml;
}