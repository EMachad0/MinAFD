package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StepController implements Initializable {

    @FXML public Button btnAnte;
    @FXML public Button btnNext;
    @FXML public Button btnEnd;
    @FXML public Label labelStepNumber;
    @FXML public ImageView imageView;
    @FXML public VBox vBox;

    private AFD afd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEnd.setOnAction(evt -> {
            afd.geraPng();
            App.closeStage(evt);
        });
    }

    public void setAFD(AFD afd) {
        this.afd = afd;
    }
}
