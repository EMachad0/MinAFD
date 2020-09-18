package elitonlais.controller;

import elitonlais.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InputNodeDialog implements Initializable {

    private String dado = null;

    @FXML public Button btnOk;
    @FXML public TextField tf1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOk.setOnAction(evt -> {
            dado = tf1.getText();
            App.closeStage(evt);
        });
    }

    public String getDado() {
        return dado;
    }
}
