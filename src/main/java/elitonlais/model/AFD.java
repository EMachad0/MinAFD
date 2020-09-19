package elitonlais.model;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static guru.nidi.graphviz.model.Factory.*;

public class AFD {

    private String estadoInicial;
    private final Set<Character> alfabeto;
    private final Grafo grafo;
    private final Set<String> estadosFinais;

    public AFD(String estadoInicial, Set<Character> alfabeto, Grafo grafo, Set<String> estadosFinais) {
        this.estadoInicial = estadoInicial;
        this.alfabeto = alfabeto;
        this.grafo = grafo;
        this.estadosFinais = estadosFinais;
    }

    public boolean executa(String palavra) {
        boolean terminouPalavra = true;
        String estado = estadoInicial;
//        for (int i = 0; i < palavra.length(); i++) {
//            System.out.println(estado);
//
//            char transicao = palavra.charAt(i);
//            if (!grafo.containTransition(estado, transicao)) {
//                terminouPalavra = false;
//                break;
//            }
//            estado = grafo.getNode(estado, transicao);
//        }

        return terminouPalavra && estadosFinais.contains(estado);
    }

    public Set<String> getEstados() {
        return grafo.getNodes();
    }

    public Grafo getGrafo() {
        return grafo;
    }

    public Set<String> getEstadosFinais() {
        return estadosFinais;
    }

    public Set<Character> getAlfabeto() {
        return alfabeto;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public void geraPng() {
        Map<String, Node> nodes = new TreeMap<>();
        for (String a : grafo.getNodes()) {
            if (estadosFinais.contains(a)) nodes.put(a, node(a).with(Label.nodeName(), Shape.DOUBLE_CIRCLE));
            else nodes.put(a, node(a).with(Label.nodeName(), Shape.CIRCLE));
        }

        Graph g = graph("MdsLfa").directed().graphAttr().with(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT));

        for (String a : grafo.getNodes()) {
            for (String b : grafo.getNodes()) {
                List<Aresta> list = grafo.getEdge(a, b);
                StringJoiner joiner = new StringJoiner(",");
                for (Aresta aresta : list) joiner.add(aresta.toString());
                String val = joiner.toString();
                g = g.with(nodes.get(a).link(to(nodes.get(b)).with(Label.of(String.valueOf(val)))));
            }
        }

        g = g.with(node("").with(Shape.POINT).link(to(nodes.get(estadoInicial))));

        try {
            Graphviz.fromGraph(g).width(1342).render(Format.PNG).toFile(new File("graph.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
