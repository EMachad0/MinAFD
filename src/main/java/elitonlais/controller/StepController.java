package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Grid;
import elitonlais.model.Pair;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StepController implements Initializable {

    @FXML public Button btnAnte;
    @FXML public Button btnNext;
    @FXML public Button btnEnd;
    @FXML public Label labelStepNumber;
    @FXML public Label labelMainText;
    @FXML public Label labelLista;
    @FXML public GridPane gridPane;

    private AFD afd;
    private Grid grid;

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

    public void initGridPane() {
        List<String> estados = new ArrayList<>(afd.getEstados());
        grid = new Grid(gridPane, estados.size(), estados.size());

        // y axis
        for (int i = 1; i < estados.size(); i++) {
            Label l = new Label(estados.get(i));
            l.setId("state");
            grid.add(l, i-1, 0);
        }

        // x axis
        for (int i = 1; i < estados.size(); i++) {
            Label l = new Label(estados.get(i-1));
            l.setId("state");
            grid.add(l, estados.size()-1, i);
        }

        // System.out.println(gridPane.getChildren());
        for (int i = 0; i < estados.size()-1; i++) {
            for (int j = 1; j <= i+1; j++) {
                Label l = new Label();
                grid.add(l, i, j);
            }
        }
    }

    public void setAFD(AFD afd) {
        this.afd = afd;
    }

    public int getPasso() {
        return Integer.parseInt(labelStepNumber.getText());
    }
}
