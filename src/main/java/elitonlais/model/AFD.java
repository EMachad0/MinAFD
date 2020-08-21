package elitonlais.model;

import java.util.Set;

public class AFD {

    private final String estadoInicial;
    private final String alfabeto;
    private final Grafo grafo;
    private final Set<String> estadosFinais;

    public AFD(String estadoInicial, String alfabeto, Grafo grafo, Set<String> estadosFinais) {
        this.estadoInicial = estadoInicial;
        this.alfabeto = alfabeto;
        this.grafo = grafo;
        this.estadosFinais = estadosFinais;
    }

    public boolean executa(String palavra) {
        boolean terminouPalavra = true;
        String estado = estadoInicial;
        for (int i = 0; i < palavra.length(); i++) {
            System.out.println(estado);

            String transicao = String.valueOf(palavra.charAt(i));
            if (!grafo.containTransition(estado, transicao)) {
                terminouPalavra = false;
                break;
            }
            estado = grafo.getNode(estado, transicao);
        }

        return terminouPalavra && estadosFinais.contains(estado);
    }

//    public void geraPng() {
//        Map<String, Node> nodes = new TreeMap<>();
//        for (String a : grafo.getNodes()) {
//            if (estadosFinais.contains(a)) nodes.put(a, node(a).with(Label.nodeName(), Shape.DOUBLE_CIRCLE));
//            else nodes.put(a, node(a).with(Label.nodeName(), Shape.CIRCLE));
//        }
//
//        Graph g = graph("MdsLfa").directed();
//        for (Pair<String, String> p : grafo.getEdges().keySet()) {
//            for (String edge : grafo.getEdge(p)) {
//                g = g.with(nodes.get(p.getFi()).link(to(nodes.get(p.getSe())).with(Label.of(edge))));
//            }
//        }
//
//        g = g.with(node("").with(Shape.POINT).link(to(nodes.get(estadoInicial))));
//
//        try {
//            Graphviz.fromGraph(g).width(400).render(Format.PNG).toFile(new File("graph.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
