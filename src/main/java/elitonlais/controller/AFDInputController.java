package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Grafo;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

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
    @FXML public TableView<Integer> myTable;

    private String alfa;
    private Grafo grafo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnTabela.setOnAction(event -> {
            alfa = fieldAlfabeto.getText();
            int numEstados = Integer.parseInt(fieldNumState.getText());

            grafo = new Grafo();
            grafo.addNode(numEstados);

            System.out.println("numEstados: " + numEstados);
            System.out.println("alfa: " + alfa);

            // Table
            myTable.getItems().clear();
            myTable.getColumns().clear();

            myTable.setEditable(true);
            myTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

            for (int i = 0; i < numEstados; i++) myTable.getItems().add(i);

            List<String> est = new ArrayList<>();
            for (int j = 0; j < numEstados; j++) est.add("q" + j);

            TableColumn <Integer, String> indices = new TableColumn<>();
            indices.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(est.get(rowIndex));
            });
            myTable.getColumns().add(indices);

            for (int i = 0; i < alfa.length(); i++) {
                TableColumn<Integer, String> column = new TableColumn<>(String.valueOf(alfa.charAt(i)));
                column.setCellValueFactory(cellData -> new ReadOnlyStringWrapper());
                column.setCellFactory(TextFieldTableCell.forTableColumn());

                column.setOnEditCommit(evt -> {
                    String a = indices.getCellData(evt.getRowValue());
                    String v = evt.getTableColumn().getText();
                    String b = evt.getNewValue();
                    grafo.addDirEdge(a, b, v);
                });

                myTable.getColumns().add(column);
            }
        });

        btnSimplifica.setOnAction(event -> {
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
