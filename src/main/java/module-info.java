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
}