module fr.univ_amu.iut {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires java.desktop;

    opens fr.univ_amu.iut to javafx.fxml;
    exports fr.univ_amu.iut;
    exports fr.univ_amu.iut.client;
    opens fr.univ_amu.iut.client to javafx.fxml;
}