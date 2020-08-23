package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Grafo;
import elitonlais.model.Grid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

public class AFDInputController implements Initializable {

    @FXML public TextField fieldAlfabeto;
    @FXML public TextField fieldNumState;
    @FXML public TextField fieldEstadoInicial;
    @FXML public TextField fieldEstadosFinais;
    @FXML public Button btnTabela;
    @FXML public Button btnSimplifica;
    @FXML public GridPane gridPane;

    private final Set<Character> alfa = new TreeSet<>();
    private Grid grid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnTabela.setOnAction(event -> {
            int numEstados = Integer.parseInt(fieldNumState.getText());

            System.out.println("numEstados: " + numEstados);
            System.out.println("alfa: " + alfa);

            // Grid
            grid = new Grid(gridPane, numEstados+1);
            for (int i = 0; i < numEstados; i++) {
                TextField tf1 = new TextField(), tf2 = new TextField();
                tf1.setText("Q" + i);
                tf1.setId("state");
                tf2.setText("Q" + i);
                tf2.setId("state");
                tf1.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (tf1.isFocused()) tf2.setText(newValue);
                });
                tf2.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (tf2.isFocused()) tf1.setText(newValue);
                });
                grid.add(tf1, i+1, 0);
                grid.add(tf2, 0, i+1);
            }

            for (int i = 1; i <= numEstados; i++) {
                for (int j = 1; j <= numEstados; j++) {
                    TextField tf = new TextField();
                    tf.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue.length() > oldValue.length()) {
                            char novo = newValue.charAt(newValue.length()-1);
                            if (tf.isFocused()) {
                                fieldNumState.requestFocus();
                                if (!alfa.contains(novo) || oldValue.indexOf(novo) != -1) tf.setText(oldValue);
                                tf.requestFocus();
                            }
                        }
                    });
                    grid.add(tf, i, j);
                }
            }
        });

        btnSimplifica.setOnAction(event -> {
            Grafo grafo = new Grafo();

            for (int i = 1; i < grid.getSize(); i++) grafo.addNode(((TextField) grid.getNode(i, 0)).getText());

            for (int i = 1; i < grid.getSize(); i++) {
                for (int j = 1; j < grid.getSize(); j++) {
                    String a = ((TextField) grid.getNode(i, 0)).getText();
                    String b = ((TextField) grid.getNode(0, j)).getText();
                    String vs = ((TextField) grid.getNode(i, j)).getText();
                    for (char c : vs.toCharArray()) {
                        grafo.addDirEdge(a, b, c);
                    }
                }
            }
            System.out.println(grafo);

            String estadoInicial = fieldEstadoInicial.getText();

            String[] arrEstadosFinais = fieldEstadosFinais.getText().split(" ");
            Set<String> estadosFinais = new TreeSet<>(new StringSizeFirstComparator());
            estadosFinais.addAll(Arrays.asList(arrEstadosFinais));

            AFD afd = new AFD(estadoInicial, alfa, grafo, estadosFinais);

            // afd.geraPng();
            try {
                App.showStepView(afd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
