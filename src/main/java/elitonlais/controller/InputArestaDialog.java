package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.Aresta;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InputArestaDialog implements Initializable {

    private Aresta aresta = null;

    @FXML public Button btnOk;
    @FXML public TextField tf1;
    @FXML public TextField tf2;
    @FXML public TextField tf3;
    @FXML public TextField tf4;
    @FXML public TextField tf5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnOk.setOnAction(evt -> {
            char fi = (tf3.getText().isEmpty())? 'ε':tf3.getText().charAt(0);
            char se = (tf4.getText().isEmpty())? 'ε':tf4.getText().charAt(0);
            String th = (tf5.getText().isEmpty())? "ε":tf5.getText();
            aresta = new Aresta(tf1.getText(), tf2.getText(), fi, se, th);
            App.closeStage(evt);
        });
    }

    public Aresta getAresta() {
        return aresta;
    }
}
