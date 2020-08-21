module elitonlais {
    requires javafx.controls;
    requires javafx.fxml;

    opens elitonlais to javafx.fxml;
    opens elitonlais.controller to javafx.fxml;
    opens elitonlais.model to javafx.fxml;

    exports elitonlais;
    exports elitonlais.controller;
    exports elitonlais.model;
}