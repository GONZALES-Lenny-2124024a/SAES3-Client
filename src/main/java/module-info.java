module fr.univ_amu.iut {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.univ_amu.iut to javafx.fxml;
    exports fr.univ_amu.iut;
    exports fr.univ_amu.iut.client;
    opens fr.univ_amu.iut.client to javafx.fxml;
}