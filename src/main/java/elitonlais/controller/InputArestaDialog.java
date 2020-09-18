package elitonlais.controller;

import elitonlais.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InputArestaDialog implements Initializable {

    private String dado = null;

    @FXML public Button btnOk;
    @FXML public TextField tf1;
    @FXML public TextField tf2;
    @FXML public TextField tf3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOk.setOnAction(evt -> {
            dado = tf1.getText() + "," + tf2.getText() + "," + tf3.getText();
            App.closeStage(evt);
        });
    }

    public String getDado() {
        return dado;
    }
}
