package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Grid;
import elitonlais.model.Minimizador;
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
    private Minimizador minimizador;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnAnte.setOnAction(event -> atualiza(getPasso() - 1));
        btnNext.setOnAction(event -> atualiza(getPasso() + 1));

        btnEnd.setOnAction(event -> {
            minimizador.getAFD().geraPng();

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

    public void atualiza(int atual) {
        atual = Math.max(0, Math.min(minimizador.getQuantPasso()-1, atual));
        labelStepNumber.setText(String.valueOf(atual));

        List<String> estados = new ArrayList<>(afd.getEstados());

        String mainText = minimizador.getPassoTexto(atual);
        Map<Pair<String, String>, List<Pair<String, String>>> lista = minimizador.getPassoListas(atual);
        Map<Pair<String, String>, String> m = minimizador.getPassoTabela(atual);

        labelMainText.setText(mainText);

        for (int i = 0; i < estados.size()-1; i++) {
            for (int j = 1; j <= i+1; j++) {
                String qi = estados.get(i+1);
                String qj = estados.get(j-1);
                ((Labeled) grid.getNode(i, j)).setText(m.get(new Pair<>(qj, qi)));
            }
        }

        StringBuilder listaText = new StringBuilder("Listas:\n");
        for (Pair<String, String> pair : lista.keySet()) {
            listaText.append(pair).append(" -> { ");
            for (Pair<String, String> p : lista.get(pair)) {
                listaText.append(p).append(", ");
            }
            listaText.deleteCharAt(listaText.length() - 2);
            listaText.append("};\n");
        }
        labelLista.setText(listaText.toString());
    }

    public void setAFD(AFD afd) {
        this.afd = afd;
        minimizador = new Minimizador(afd);
        initGridPane();
        atualiza(0);
    }

    public int getPasso() {
        return Integer.parseInt(labelStepNumber.getText());
    }
}
