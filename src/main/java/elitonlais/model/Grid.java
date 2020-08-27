package elitonlais.model;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;

public class Grid {

    private final int lin, col;
    private final GridPane gridPane;
    private final Node[][] matriz;

    public Grid(GridPane gridPane, int lin, int col) {
        this.lin = lin;
        this.col = col;
        this.gridPane = gridPane;

        gridPane.getChildren().clear();
        matriz = new Node[lin][col];
    }

    public void add(Node node, int i, int j) {
        if (i < 0 || i >= lin) throw new ArrayIndexOutOfBoundsException("Invalid I");
        if (j < 0 || j >= col) throw new ArrayIndexOutOfBoundsException("Invalid J");
        if (matriz[i][j] != null) throw new ArrayStoreException("Cell " + i + " " + j + " already taken");
        gridPane.add(node, j, i);
        matriz[i][j] = node;
    }

    public Node getNode(int i, int j) {
        if (i < 0 || i >= lin) throw new ArrayIndexOutOfBoundsException("Invalid I");
        if (j < 0 || j >= col) throw new ArrayIndexOutOfBoundsException("Invalid J");
        return matriz[i][j];
    }

    public void setText(String text, int i, int j) {
        Node node = getNode(i, j);
        if (node instanceof Labeled) ((Labeled) node).setText(text);
    }

    public int getLin() {
        return lin;
    }

    public int getCol() {
        return col;
    }
}
