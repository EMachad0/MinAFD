package elitonlais.controller;

import com.kitfox.svg.A;
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
            aresta = new Aresta(tf1.getText(), tf2.getText(), tf3.getText().charAt(0), tf4.getText().charAt(0), tf5.getText().charAt(0));
            App.closeStage(evt);
        });
    }

    public Aresta getAresta() {
        return aresta;
    }
}
