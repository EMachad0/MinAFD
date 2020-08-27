package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Grafo;
import elitonlais.model.Grid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
        fieldNumState.setTextFormatter(new TextFormatter<>(c -> (c.getControlNewText().matches("([1-9][0-9]*)?")) ? c : null));
        fieldAlfabeto.setTextFormatter(new TextFormatter<>(c -> (c.getControlNewText().matches("([A-Z]|[a-z]|[0-9])*")) ? c : null));
        fieldAlfabeto.textProperty().addListener((observable, oldValue, newValue) -> {
            if (fieldAlfabeto.isFocused() && newValue.length() < oldValue.length()) {
                alfa.remove(oldValue.charAt(oldValue.length()-1));
            } else if (fieldAlfabeto.isFocused() && newValue.length() > oldValue.length()) {
                char novo = newValue.charAt(newValue.length()-1);
                fieldNumState.requestFocus();
                if (alfa.contains(novo)) fieldAlfabeto.setText(oldValue);
                else alfa.add(novo);
                fieldAlfabeto.requestFocus();
            }
            // System.out.println(alfa);
        });

        btnTabela.setOnAction(event -> {
            int numEstados = Integer.parseInt(fieldNumState.getText());

            System.out.println("numEstados: " + numEstados);
            System.out.println("alfa: " + alfa);

            // Grid
            grid = new Grid(gridPane, numEstados+1, alfa.size()+1);
            for (int i = 0; i < numEstados; i++) {
                TextField tf = new TextField();
                tf.setText("q" + i);
                tf.setTextFormatter(new TextFormatter<>(c -> (c.getControlNewText().matches("([A-Z]|[a-z]|[0-9])*")) ? c : null));
                tf.setId("state");
                grid.add(tf, i+1, 0);
            }

            List <Character> alfalist = new ArrayList<>(alfa);
            for (int i = 0; i < alfalist.size(); i++) {
                Label l = new Label();
                l.setText(String.valueOf(alfalist.get(i)));
                l.setId("state");
                grid.add(l, 0, i+1);
            }

            for (int i = 1; i <= numEstados; i++) {
                for (int j = 1; j <= alfalist.size(); j++) {
                    TextField tf = new TextField();
                    tf.setTextFormatter(new TextFormatter<>(c -> (c.getControlNewText().matches("([A-Z]|[a-z]|[0-9])*")) ? c : null));
                    grid.add(tf, i, j);
                }
            }
        });

        btnSimplifica.setOnAction(event -> {
            StringBuilder alertMsg = new StringBuilder();
            Grafo grafo = new Grafo();

            for (int i = 1; i < grid.getLin(); i++) grafo.addNode(((TextField) grid.getNode(i, 0)).getText());

            boolean validE = true;
            for (int i = 1; i < grid.getLin(); i++) {
                for (int j = 1; j < grid.getCol(); j++) {
                    String a = ((TextField) grid.getNode(i, 0)).getText();
                    String b = ((TextField) grid.getNode(i, j)).getText();
                    char c = ((Label) grid.getNode(0, j)).getText().charAt(0);
                    if (!grafo.containNode(b)) {
                        alertMsg.append(c).append(" ");
                        validE = false;
                    } else grafo.addDirEdge(a, b, c);
                }
            }
            if (!validE) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Os estados " + alertMsg.toString() + "não existem!");
                alert.show();
            }
            System.out.println(grafo);

            boolean total = true;
            for (String node : grafo.getNodes()) {
                for (Character c : alfa) {
                    if (!grafo.containTransition(node, c)) total = false;
                }
            }
            if (!total) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Automato não total");
                alert.show();
            }

            boolean validEI = true;
            String estadoInicial = fieldEstadoInicial.getText();
            if (!grafo.containNode(estadoInicial)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("O Estado Inicial " + estadoInicial + " é Invalido");
                alert.show();
                validEI = false;
            }

            String[] arrEstadosFinais = fieldEstadosFinais.getText().split(" ");
            Set<String> estadosFinais = new TreeSet<>(new StringSizeFirstComparator());
            estadosFinais.addAll(Arrays.asList(arrEstadosFinais));

            boolean validEF = true;
            alertMsg = new StringBuilder();
            for (String s : estadosFinais) {
                if (!grafo.containNode(s)) {
                    validEF = false;
                    alertMsg.append(s).append(" ");
                }
            }
            if (!validEF) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Os estados finais " + alertMsg + "Invalido");
                alert.show();
            }

            if (validE && validEI && validEF && total) {
                AFD afd = new AFD(estadoInicial, alfa, grafo, estadosFinais);
                try {
                    App.showStepView(afd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
