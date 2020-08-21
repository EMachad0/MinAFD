module elitonlais {
    requires javafx.controls;
    requires javafx.fxml;
    requires graphviz.java;

    opens elitonlais to javafx.fxml;
    opens elitonlais.controller to javafx.fxml;
    opens elitonlais.model to javafx.fxml;

    exports elitonlais;
    exports elitonlais.controller;
    exports elitonlais.model;
}