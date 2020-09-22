package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Grafo;
import elitonlais.model.Simulador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class AFDInputController implements Initializable {

    @FXML public Button btnAddNode;
    @FXML public Button btnAddEdge;
    @FXML public Button btnClear;
    @FXML public Button btnExecute;
    @FXML public ImageView imageView;
    @FXML public TextField tfFita;
    @FXML public TextArea taInput;
    @FXML public TextFlow taOutput;

    private AFD afd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        afd = new AFD(null,  new TreeSet<>(), new Grafo(), new TreeSet<>(new StringSizeFirstComparator()));
        atualizaImagem();

        btnAddNode.setOnAction(e -> {
            try {
                InputNodeDialog d = (InputNodeDialog) App.showDialog("InputNodeDialog", "Novo Estado");
                if(!d.getText().equals("")){
                    afd.getGrafo().addNode(d.getText());
                    if (d.isInicial()) afd.setEstadoInicial(d.getText());
                    if (d.isFinal()) afd.getEstadosFinais().add(d.getText());
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            atualizaImagem();
        });

        btnAddEdge.setOnAction(e -> {
            try {
                InputArestaDialog d = (InputArestaDialog) App.showDialog("InputArestaDialog", "Nova Aresta");
                if(d.getAresta() != null){
                    if(afd.getGrafo().getNodes().contains(d.getAresta().getA()) &&
                            afd.getGrafo().getNodes().contains(d.getAresta().getB()))
                        afd.getGrafo().addDirEdge(d.getAresta());
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            atualizaImagem();
        });

        btnClear.setOnAction(e -> {
            afd = new AFD(null, new TreeSet<>(), new Grafo(), new TreeSet<>(new StringSizeFirstComparator()));
            atualizaImagem();
        });

        btnExecute.setOnAction(e -> {
            Simulador simulador = new Simulador(afd);
            String[] inputs = taInput.getText().split("\n");
            taOutput.getChildren().clear();
            for (String input : inputs) {
                boolean res = simulador.testa(input);
                Text t = new Text("Palavra " + ((res)? "":"Não ") + "Aceita\n");
                t.setId(((res)? "":"nao") + "Aceito");
                t.setStyle("-fx-fill: " + ((res)? "#3ae073":"#e03a5b"));
                taOutput.getChildren().add(t);
            }

            if (!tfFita.getText().equals("")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(App.class.getResource("step.fxml"));
                try {
                    Parent parent = loader.load();

                    ((StepController) loader.getController()).setFita(tfFita.getText());
                    ((StepController) loader.getController()).setSimulador(new Simulador(afd));
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.setTitle("Visualização passo a passo");
                    stage.showAndWait();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    public void atualizaImagem() {
        afd.geraPng();

        Image image = new Image("file:graph.png");
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }
}
