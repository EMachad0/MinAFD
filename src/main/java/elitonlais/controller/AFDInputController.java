package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Aresta;
import elitonlais.model.Grafo;
import elitonlais.model.Grid;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
            grid = new Grid(gridPane, numEstados+1, numEstados+1);

            for (int i = 0; i < numEstados; i++) {
                TextField tf1 = new TextField(), tf2 = new TextField();
                tf1.setText("q" + i);
                tf1.setId("state");
                tf2.setText("q" + i);
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
                    Button btn = new Button();
                    btn.setOnAction(evt -> {
                        try {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(App.class.getResource("InputArestaDialog.fxml"));
                            Parent parent = loader.load();
                            InputArestaDialog d = loader.getController();
                            Scene scene = new Scene(parent, 300, 200);
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.setScene(scene);
                            stage.showAndWait();
                            btn.setText(d.getDado());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    grid.add(btn, i, j);
                }
            }
        });

        btnSimplifica.setOnAction(event -> {
            Grafo grafo = new Grafo();

            for (int i = 1; i < grid.getLin(); i++) grafo.addNode(((TextField) grid.getNode(i, 0)).getText());

            boolean deter = true;
            for (int i = 1; i < grid.getLin(); i++) {
                for (int j = 1; j < grid.getCol(); j++) {
                    String a = ((TextField) grid.getNode(i, 0)).getText();
                    String b = ((TextField) grid.getNode(0, j)).getText();
                    String vs = ((Button) grid.getNode(i, j)).getText();

                    System.out.println(vs);
                    String[] arr = vs.split(",");
                    char fi = arr[0].charAt(0);
                    char se = arr[1].charAt(0);
                    char th = arr[2].charAt(0);
                    if (grafo.containTransition(a, fi, se, th)) deter = false;
                    else grafo.addDirEdge(new Aresta(a, b, fi, se, th));
                }
            }
            if (!deter) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Automato não deterministíco");
                alert.show();
            }
            // System.out.println(grafo);

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
            StringBuilder alertMsg = new StringBuilder();
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

            if (deter && validEI && validEF) {
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
