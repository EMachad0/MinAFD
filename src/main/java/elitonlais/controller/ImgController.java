package elitonlais.controller;

import elitonlais.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ImgController implements Initializable {

    @FXML public ImageView imgView;
    @FXML public Button btnEnd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image("file:graph.png");
        imgView.setImage(image);
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        imgView.setCache(true);
        imgView.setFitHeight(700);

        btnEnd.setOnAction(event -> {
            try {
                App.setRoot("afdinput");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
