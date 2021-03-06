package elitonlais.model;

import elitonlais.controller.StringSizeFirstComparator;
import guru.nidi.graphviz.attribute.Color;
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

public class Simulador {

    private final AFD afd;

    private String nola;
    private Aresta arla;
    private Set<String> novi = new TreeSet<>(new StringSizeFirstComparator());
    private Set<Aresta> arvi = new TreeSet<>();
    private Stack<Character> pilha = new Stack<>();

    public Simulador(AFD afd) {
        this.afd = afd;
    }

    public boolean testa(String fita) {
        return testa(fita, true);
    }

    public boolean testa(String fita, boolean fitaCompleta) {
        novi = new TreeSet<>(new StringSizeFirstComparator());
        arvi = new TreeSet<>();
        pilha = new Stack<>();

        String atual = afd.getEstadoInicial();
        marca(atual);
        boolean terminouFita = true;
        for (char c : fita.toCharArray()) {
            boolean achouAresta = false;

            char topo = (pilha.isEmpty())? 'ε':pilha.peek();

            for (Aresta ar : afd.getGrafo().getAdj().get(atual)) {
                if (ar.getFi() == c && (ar.getSe() == 'ε' || ar.getSe() == topo || (ar.getSe() == '?' && pilha.isEmpty()))) {
                    if (ar.getSe() != 'ε' && ar.getSe() != '?') pilha.pop();
                    for (char th : ar.getTh().toCharArray()) if (th != 'ε') pilha.push(th);
                    atual = ar.getB();
                    marca(atual);
                    marca(ar);
                    achouAresta = true;
                }
            }

            if (!achouAresta) {
                terminouFita = false;
                break;
            }
        }

        while (fitaCompleta && terminouFita && !afd.getEstadosFinais().contains(atual)) {
            boolean achouAresta = false;
            for (Aresta ar : afd.getGrafo().getAdj().get(atual)) {
                if ((ar.getFi() == 'ε' || ar.getFi() == '?') && (ar.getSe() == 'ε' || (ar.getSe() == '?' && pilha.isEmpty()))) {
                    atual = ar.getB();
                    marca(atual);
                    marca(ar);
                    achouAresta = true;
                }
            }

            if (!achouAresta) {
                terminouFita = false;
                break;
            }
        }

        return afd.getEstadosFinais().contains(atual) && terminouFita && pilha.isEmpty();
    }

    private void marca(String node) {
        nola = node;
        novi.add(node);
    }

    private void marca(Aresta aresta) {
        arla = aresta;
        arvi.add(aresta);
    }

    public void geraPng() {
        Graph g = graph("AFDSTEP").directed().graphAttr().with(Rank.dir(Rank.RankDir.LEFT_TO_RIGHT));

        Map<String, Node> nodes = new TreeMap<>();
        for (String a : afd.getGrafo().getNodes()) {
            if (afd.getEstadosFinais().contains(a)) nodes.put(a, node(a).with(Label.nodeName(), Shape.DOUBLE_CIRCLE));
            else nodes.put(a, node(a).with(Label.nodeName(), Shape.CIRCLE));
        }

        System.out.println(novi);
        for (String k : nodes.keySet()) {
            if (novi.contains(k)) {
                Node n = nodes.get(k);
                n = n.with((k.equals(nola))? Color.GREEN:Color.BLUE);
                nodes.put(k, n);
            }
            g = g.with(nodes.get(k));
        }

        for (String a : afd.getGrafo().getNodes()) {
            for (String b : afd.getGrafo().getNodes()) {
                List<Aresta> list = afd.getGrafo().getEdge(a, b);
                if (!list.isEmpty()) {
                    StringJoiner triste = new StringJoiner(",");
                    StringJoiner feliz = new StringJoiner(",");
                    for (Aresta aresta : list) {
                        if (aresta.equals(arla)) g = g.with(nodes.get(a).link(to(nodes.get(b)).with(Label.of(aresta.toString()), Color.GREEN)));
                        else if (arvi.contains(aresta)) feliz.add(aresta.toString());
                        else triste.add(aresta.toString());
                    }
                    if (triste.length() > 0) g = g.with(nodes.get(a).link(to(nodes.get(b)).with(Label.of(triste.toString()))));
                    if (feliz.length() > 0) g = g.with(nodes.get(a).link(to(nodes.get(b)).with(Label.of(feliz.toString()), Color.BLUE)));
                }
            }
        }

        if (afd.getEstadoInicial() != null) g = g.with(node("").with(Shape.POINT).link(to(nodes.get(afd.getEstadoInicial()))));

        try {
            Graphviz.fromGraph(g).width(804).render(Format.PNG).toFile(new File("graph.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stack<Character> getPilha() {
        return pilha;
    }
}
