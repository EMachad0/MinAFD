package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.AFD;
import elitonlais.model.Grafo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private AFD afd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        afd = new AFD(null,  new TreeSet<>(), new Grafo(), new TreeSet<>(new StringSizeFirstComparator()));
        atualizaImagem();

        btnAddNode.setOnAction(e -> {
            try {
                InputNodeDialog d = (InputNodeDialog) App.showDialog("InputNodeDialog", "Novo Estado");
                afd.getGrafo().addNode(d.getText());
                if (d.isInicial()) afd.setEstadoInicial(d.getText());
                if (d.isFinal()) afd.getEstadosFinais().add(d.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            atualizaImagem();
        });

        btnAddEdge.setOnAction(e -> {
            try {
                InputArestaDialog d = (InputArestaDialog) App.showDialog("InputArestaDialog", "Nova Aresta");
                afd.getGrafo().addDirEdge(d.getAresta());
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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("step.fxml"));

            try {
                Parent parent = loader.load();

                ((StepController) loader.getController()).setAFD(afd);
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.setTitle("Visualização passo a passo");
                stage.showAndWait();
            } catch (IOException ioException) {
                ioException.printStackTrace();
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
