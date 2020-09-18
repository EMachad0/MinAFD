package elitonlais.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            closeStage(evt);
        });
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public String getDado() {
        return dado;
    }
}
