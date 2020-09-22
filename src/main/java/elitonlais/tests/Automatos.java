package elitonlais.tests;

import elitonlais.controller.StringSizeFirstComparator;
import elitonlais.model.AFD;
import elitonlais.model.Aresta;
import elitonlais.model.Grafo;

import java.util.TreeSet;

public class Automatos {

    public static AFD atutomatoDoProfessor() {
        AFD afd = new AFD(null,  new TreeSet<>(), new Grafo(), new TreeSet<>(new StringSizeFirstComparator()));
        afd.getGrafo().addNode("q0");
        afd.getGrafo().addNode("q1");
        afd.getGrafo().addNode("q2");
        afd.setEstadoInicial("q0");
        afd.getEstadosFinais().add("q2");
        afd.getGrafo().addDirEdge(new Aresta("q0", "q0", 'a', 'B', 'ε'));
        afd.getGrafo().addDirEdge(new Aresta("q0", "q1", 'b', 'ε', 'B'));
        afd.getGrafo().addDirEdge(new Aresta("q1", "q1", 'b', 'ε', 'B'));
        afd.getGrafo().addDirEdge(new Aresta("q1", "q2", '?', '?', 'ε'));
        afd.getGrafo().addDirEdge(new Aresta("q0", "q2", '?', '?', 'ε'));
        return afd;
    }
}
