package elitonlais.controller;

import elitonlais.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InputNodeDialog implements Initializable {

    @FXML public Button btnOk;
    @FXML public TextField tf1;
    @FXML public CheckBox box1;
    @FXML public CheckBox box2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOk.setOnAction(App::closeStage);
    }

    public String getText() {
        return tf1.getText();
    }

    public Boolean isInicial() {
        return box1.isSelected();
    }

    public Boolean isFinal() {
        return box2.isSelected();
    }
}
