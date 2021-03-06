package elitonlais.controller;

import elitonlais.App;
import elitonlais.model.Simulador;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class StepController implements Initializable {

    @FXML public Button btnAnte;
    @FXML public Button btnNext;
    @FXML public Button btnEnd;
    @FXML public Label labelFita;
    @FXML public Label labelResultado;
    @FXML public ImageView imageView;
    @FXML public VBox vBox;

    private int cont = 0;
    private String fita;
    private Simulador s;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnAnte.setOnAction(evt -> mudaPasso(cont-1));
        btnNext.setOnAction(evt -> mudaPasso(cont+1));

        btnEnd.setOnAction(App::closeStage);
    }

    public void mudaPasso(int passo) {
        this.cont = Math.max(0, Math.min(fita.length(), passo));
        labelFita.setText(fita.substring(0, cont));
        boolean res = s.testa(fita.substring(0, cont), fita.length() == cont);
        if (cont == fita.length()) labelResultado.setText("Palavra " + ((res)? "":"Não ") + "Aceita");
        else labelResultado.setText("");
        if(res) labelResultado.setStyle("-fx-text-fill: #3ae073");
        else labelResultado.setStyle("-fx-text-fill: #e03a5b");

        atualizaImagem();
        atualizaPilha();
    }

    public void atualizaPilha() {
        Stack<Character> pilha = s.getPilha();

        vBox.getChildren().clear();
        pilha.forEach(c -> vBox.getChildren().add(0, new Label(String.valueOf(c))));
    }

    public void atualizaImagem() {
        s.geraPng();

        Image image = new Image("file:graph.png");
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }

    public void setFita(String fita) {
        this.fita = fita;
    }

    public void setSimulador(Simulador s) {
        this.s = s;
        atualizaImagem();
    }
}
