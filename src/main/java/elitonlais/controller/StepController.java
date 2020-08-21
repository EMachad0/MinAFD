package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StepController implements Initializable {

    @FXML public Button btnAnte;
    @FXML public Button btnNext;
    @FXML public Button btnEnd;
    @FXML public Label labelStepNumber;
    @FXML public Label labelMainText;
    @FXML public GridPane gridPane;

    private AFD afd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEnd.setOnAction(event -> {
            afd.geraPng();

            try {
                App.setRoot("img");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void setAFD(AFD afd) {
        this.afd = afd;
    }
}
