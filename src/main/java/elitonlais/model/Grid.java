package elitonlais.model;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class Grid {

    private static final double CELLSIZE = 50;
    private static final Font FONT = new Font("Cambria", 18);

    private final int size;
    private final GridPane gridPane;
    private final Node[][] matriz;

    public Grid(GridPane gridPane, int size) {
        this.size = size;
        this.gridPane = gridPane;

        matriz = new Node[size][size];
    }

    public void add(Node node, int i, int j) {
        if (i < 0 || i > size) throw new ArrayIndexOutOfBoundsException("Invalid I");
        if (j < 0 || j > size) throw new ArrayIndexOutOfBoundsException("Invalid J");
        if (matriz[i][j] != null) throw new ArrayStoreException("Cell " + i + " " + j + " already taken");
        gridPane.add(node, j, i);
        matriz[i][j] = node;
    }

    public Node getNode(int i, int j) {
        if (i < 0 || i > size) throw new ArrayIndexOutOfBoundsException("Invalid I");
        if (j < 0 || j > size) throw new ArrayIndexOutOfBoundsException("Invalid J");
        return matriz[i][j];
    }

    public void setText(String text, int i, int j) {
        Node node = getNode(i, j);
        if (node instanceof Labeled) ((Labeled) node).setText(text);
    }

    public static Label makeStandardLabel() {
        Label l = new Label();
        l.setPrefSize(CELLSIZE, CELLSIZE);
        l.setAlignment(Pos.CENTER);
        l.setFont(FONT);
        return l;
    }

    public static TextField makeStandardTextField() {
        TextField t = new TextField();
        t.setPrefSize(CELLSIZE, CELLSIZE);
        t.setAlignment(Pos.CENTER);
        t.setFont(FONT);
        return t;
    }
}
