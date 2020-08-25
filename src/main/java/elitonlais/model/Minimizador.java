package elitonlais.model;

import elitonlais.controller.StringSizeFirstComparator;

import java.util.*;

public class Minimizador {

    private AFD afd;

    private final List<String> passoTexto;
    private final List<Map<Pair<String, String>, String>> passoTabela;
    private final List<Map<Pair<String, String>, List<Pair<String, String>>>> passoListas;

    public Minimizador(AFD afd) {
        this.afd = afd;

        passoTexto = new ArrayList<>();
        passoTabela = new ArrayList<>();
        passoListas = new ArrayList<>();

        minimiza();
    }

    private void minimiza() {
        Grafo grafo = afd.getGrafo();

        Map<Pair<String, String>, List<Pair<String, String>>> lista = new TreeMap<>();
        Map<Pair<String, String>, String> m = new TreeMap<>();

        // passo 0
        Set<String> visto = new TreeSet<>(new StringSizeFirstComparator());
        marcaVisto(visto, grafo, afd.getEstadoInicial());

        Set<String> praRemover = new TreeSet<>(new StringSizeFirstComparator());
        praRemover.addAll(afd.getEstados());
        praRemover.removeAll(visto);

        StringBuilder s = new StringBuilder();
        if (!praRemover.isEmpty()) {
            s.append("Removendo estados inúteis:\n");
            for (String e : praRemover) {
                s.append("\tEstado ").append(e).append(" removido;\n");
                grafo.removeNode(e);
                afd.getEstadosFinais().remove(e);
                System.out.println("Removido: " + e);
            }
            s.append('\n');
        }

        List<String> estados = new ArrayList<>(afd.getEstados());
        Set<String> finais = afd.getEstadosFinais();

        for (int i = 0; i < estados.size(); i++) {
            for (int j = i+1; j < estados.size(); j++) {
                String qi = estados.get(i);
                String qj = estados.get(j);

                boolean iFinal = finais.contains(qi);
                boolean jFinal = finais.contains(qj);
                if ((iFinal && !jFinal) || (!iFinal && jFinal)) {
                    m.put(new Pair<>(qi, qj), "X");
                } else {
                    m.put(new Pair<>(qi, qj), "");
                }
            }
        }

        s.append("Marcação dos estados do tipo:\n\t{Estado final, Estado não final}");
        passoTexto.add(s.toString());
        addPassoTabela(m);
        addPassoListas(lista);

        // Passo 1..N:
        Set<Character> alfa = afd.getAlfabeto();

        for (int i = 0; i < estados.size(); i++) {
            for (int j = i+1; j < estados.size(); j++) {
                String qi = estados.get(i);
                String qj = estados.get(j);
                Pair<String, String> qiqj = new Pair<>(qi, qj);

                if (m.get(qiqj).equals("")) {
                    // Novo Passo: Analisando par nao marcado
                    s = new StringBuilder(qiqj + ":\n");

                    for (Character c : alfa) {
                        String pi = grafo.getNode(qi, c);
                        String pj = grafo.getNode(qj, c);

                        Pair<String, String> pipj = new Pair<>(pi, pj);
                        if (pi.compareTo(pj) > 0) pipj = pipj.swap();

                        s.append("Delta(").append(qi).append(", ").append(c).append(") = ").append(pi).append(";\n");
                        s.append("Delta(").append(qj).append(", ").append(c).append(") = ").append(pj).append(";\n");

                        if (pi.equals(pj)) {
                            s.append("Trivialmente equivalente\n");
                        } else if (m.get(pipj).equals("X")) {
                            s.append("O par ").append(pipj).append(" é marcado\n");
                            s.append("Logo ").append(qiqj).append(" é marcado\n");
                            m.put(qiqj, "X");

                            if (lista.containsKey(qiqj)) {
                                s.append("Marcando lista de ").append(pipj).append(":\n");
                                for (Pair<String, String> pair : lista.get(qiqj)) {
                                    m.put(pair, "X");
                                    s.append("\t").append(pair).append(" for marcado!");
                                }
                            }
                            break;
                        } else {
                            if (!lista.containsKey(pipj)) lista.put(pipj, new ArrayList<>());
                            if (!lista.get(pipj).contains(qiqj)) lista.get(pipj).add(qiqj);
                            s.append("O par ").append(pipj).append(" é não marcado\n");
                            s.append("Adicionando ").append(qiqj).append(" na lista de ").append(pipj).append(";\n");
                        }
                        s.append('\n');
                    }
                    addPassoTabela(m);
                    addPassoListas(lista);
                    passoTexto.add(s.toString());
                }
            }
        }

        // Passo final
        s = new StringBuilder(new StringBuilder("Unificando Estados:\n"));
        Grafo g = new Grafo();
        Map <String, String> mapa = new TreeMap<>(new StringSizeFirstComparator());
        for (int i = 0; i < estados.size(); i++) {
            for (int j = i+1; j < estados.size(); j++) {
                String qi = estados.get(i);
                String qj = estados.get(j);
                Pair<String, String> qiqj = new Pair<>(qi, qj);

                if (m.get(qiqj).equals("")) {
                    s.append("\t").append(qi).append(qj).append(": unificação dos estados ").append(qi).append(" e ").append(qj).append(";\n");
                    g.addNode(qi + qj);
                    mapa.put(qi, qi + qj);
                    mapa.put(qj, qi + qj);
                }
            }
        }

        addPassoTabela(m);
        addPassoListas(lista);
        passoTexto.add(s.toString());

        for (String estado : estados) {
            if (!mapa.containsKey(estado)) g.addNode(estado);
        }

        for (Pair<String, String> pair : grafo.getEdges().keySet()) {
            String a = pair.getFi();
            if (mapa.containsKey(a)) a = mapa.get(a);
            String b = pair.getSe();
            if (mapa.containsKey(b)) b = mapa.get(b);
            for (Character c : grafo.getEdges().get(pair)) {
                g.addDirEdge(a, b, c);
            }
        }

        String estadoInicial = mapa.getOrDefault(afd.getEstadoInicial(), afd.getEstadoInicial());
        Set<String> estadosFinais = new TreeSet<>(new StringSizeFirstComparator());
        for (String ef : afd.getEstadosFinais()) estadosFinais.add(mapa.getOrDefault(ef, ef));

        afd = new AFD(estadoInicial, afd.getAlfabeto(), g, estadosFinais);
        System.out.println("Terminou");
    }

    private void addPassoTabela(Map<Pair<String, String>, String> m) {
        int atual = passoTabela.size();
        passoTabela.add(new TreeMap<>());
        for (Pair<String, String> pair : m.keySet()) {
            Pair<String, String> np = new Pair<>(pair.getFi(), pair.getSe());
            passoTabela.get(atual).put(np, m.get(pair));
        }
    }

    private void addPassoListas(Map<Pair<String, String>, List<Pair<String, String>>> lista) {
        int atual = passoListas.size();
        passoListas.add(new TreeMap<>());
        for (Pair<String, String> pair : lista.keySet()) {
            Pair <String, String> np = new Pair<>(pair.getFi(), pair.getSe());
            passoListas.get(atual).put(np, new ArrayList<>());
            for (Pair<String, String> vals : lista.get(pair)) {
                Pair <String, String> nv = new Pair<>(vals.getFi(), vals.getSe());
                passoListas.get(atual).get(np).add(nv);
            }
        }
    }

    public String getPassoTexto(int i) {
        return passoTexto.get(i);
    }

    public Map<Pair<String, String>, String> getPassoTabela(int i) {
        return passoTabela.get(i);
    }

    public Map<Pair<String, String>, List<Pair<String, String>>> getPassoListas(int i) {
        return passoListas.get(i);
    }

    public AFD getAFD() {
        return afd;
    }

    public static void marcaVisto(Set<String> visto, Grafo g, String n) {
        visto.add(n);
        for (char c : g.getAdj().get(n).keySet()) {
            String i = g.getNode(n, c);
            if (!visto.contains(i)) marcaVisto(visto, g, i);
        }
    }
}
